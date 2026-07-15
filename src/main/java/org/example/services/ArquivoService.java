package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ArquivoRequestDTO;
import org.example.dto.response.ArquivoResponseDTO;
import org.example.enums.TipoArquivo;
import org.example.model.Arquivo;
import org.example.model.Submissao;
import org.example.repositories.ArquivoRepository;
import org.example.repositories.SubmissaoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;
    private final SubmissaoRepository submissaoRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Criar registro manualmente
    public ArquivoResponseDTO save(ArquivoRequestDTO request) {

        Submissao submissao = submissaoRepository.findById(request.getSubmissaoId())
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));

        Arquivo arquivo = Arquivo.builder()
                .nomeArquivo(request.getNomeArquivo())
                .caminho(request.getCaminho())
                .tipo(request.getTipo())
                .mimeType(request.getMimeType())
                .tamanho(request.getTamanho())
                .hashArquivo(request.getHashArquivo())
                .dataUpload(
                        request.getDataUpload() != null
                                ? request.getDataUpload()
                                : LocalDateTime.now()
                )
                .submissao(submissao)
                .build();

        return toResponse(arquivoRepository.save(arquivo));
    }

    // Upload
    public ArquivoResponseDTO upload(
            MultipartFile file,
            Long submissaoId,
            TipoArquivo tipo
    ) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new RuntimeException("Somente arquivos PDF são permitidos");
        }

        Submissao submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() ->
                        new RuntimeException("Submissão não encontrada"));

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null
                        ? file.getOriginalFilename()
                        : "arquivo.pdf"
        );

        if (originalName.contains("..")) {
            throw new RuntimeException("Nome de arquivo inválido");
        }

        try {

            Path uploadRoot = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            Path submissaoDir = uploadRoot
                    .resolve("submissoes")
                    .resolve(String.valueOf(submissaoId))
                    .normalize();

            Files.createDirectories(submissaoDir);

            String storedName =
                    UUID.randomUUID() + "_" + originalName;

            Path target = submissaoDir
                    .resolve(storedName)
                    .normalize();

            if (!target.startsWith(submissaoDir)) {
                throw new RuntimeException("Caminho de arquivo inválido");
            }

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String hashArquivo =
                    sha256(Files.readAllBytes(target));

            Arquivo arquivo = Arquivo.builder()
                    .nomeArquivo(originalName)
                    .caminho(target.toString())
                    .tipo(
                            tipo != null
                                    ? tipo
                                    : TipoArquivo.MANUSCRITO
                    )
                    .mimeType(file.getContentType())
                    .tamanho(file.getSize())
                    .hashArquivo(hashArquivo)
                    .dataUpload(LocalDateTime.now())
                    .submissao(submissao)
                    .build();

            return toResponse(
                    arquivoRepository.save(arquivo)
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Não foi possível salvar o arquivo",
                    e
            );
        }
    }

    public ArquivoResponseDTO uploadForAluno(MultipartFile file, Long submissaoId,
                                              TipoArquivo tipo, String email) {
        Submissao submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));
        if (!submissao.getTcc().getAluno().getUsuario().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Submissão não pertence ao aluno autenticado");
        }
        return upload(file, submissaoId, tipo);
    }

    // Download
    public ArquivoDownload download(Long id) {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Arquivo não encontrado"));

        try {

            Path path = Paths.get(arquivo.getCaminho())
                    .toAbsolutePath()
                    .normalize();

            Resource resource =
                    new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException(
                        "Arquivo físico não encontrado"
                );
            }

            return new ArquivoDownload(
                    resource,
                    arquivo.getNomeArquivo(),
                    arquivo.getMimeType()
            );

        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Caminho de arquivo inválido",
                    e
            );
        }
    }

    public ArquivoDownload downloadAuthorized(Long id, String email, boolean coordenador) {
        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
        assertCanAccess(arquivo.getSubmissao(), email, coordenador);
        return download(id);
    }

    public List<ArquivoResponseDTO> findBySubmissaoAuthorized(Long submissaoId, String email,
                                                               boolean coordenador) {
        Submissao submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));
        assertCanAccess(submissao, email, coordenador);
        return findBySubmissaoId(submissaoId);
    }

    private void assertCanAccess(Submissao submissao, String email, boolean coordenador) {
        if (coordenador) return;
        var tcc = submissao.getTcc();
        boolean aluno = tcc.getAluno().getUsuario().getEmail().equalsIgnoreCase(email);
        boolean orientador = tcc.getOrientador().getUsuario().getEmail().equalsIgnoreCase(email);
        boolean coorientador = tcc.getCoorientador() != null
                && tcc.getCoorientador().getUsuario().getEmail().equalsIgnoreCase(email);
        if (!aluno && !orientador && !coorientador) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado ao arquivo");
        }
    }

    // Listar todos
    public List<ArquivoResponseDTO> findAll() {

        return arquivoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Buscar por Aluno email
    public List<ArquivoResponseDTO> findByAlunoEmail(String email) {

        return arquivoRepository
                .findBySubmissaoTccAlunoUsuarioEmail(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Listar por submissão
    public List<ArquivoResponseDTO> findBySubmissaoId(Long submissaoId) {

        return arquivoRepository.findBySubmissaoId(submissaoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Buscar por ID
    public ArquivoResponseDTO findById(Long id) {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Arquivo não encontrado"));

        return toResponse(arquivo);
    }

    // Atualizar metadados
    public ArquivoResponseDTO update(
            Long id,
            ArquivoRequestDTO request
    ) {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Arquivo não encontrado"));

        Submissao submissao = submissaoRepository.findById(
                        request.getSubmissaoId())
                .orElseThrow(() ->
                        new RuntimeException("Submissão não encontrada"));

        arquivo.setNomeArquivo(request.getNomeArquivo());
        arquivo.setTipo(request.getTipo());
        arquivo.setMimeType(request.getMimeType());
        arquivo.setTamanho(request.getTamanho());
        arquivo.setSubmissao(submissao);

        arquivo.setDataUpload(
                request.getDataUpload() != null
                        ? request.getDataUpload()
                        : arquivo.getDataUpload()
        );

        return toResponse(
                arquivoRepository.save(arquivo)
        );
    }

    // Excluir
    public void deleteById(Long id) {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Arquivo não encontrado"));

        try {
            Files.deleteIfExists(
                    Paths.get(arquivo.getCaminho())
            );
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao remover arquivo físico",
                    e
            );
        }

        arquivoRepository.delete(arquivo);
    }

    private ArquivoResponseDTO toResponse(Arquivo arquivo) {

        return new ArquivoResponseDTO(
                arquivo.getId(),
                arquivo.getNomeArquivo(),
                arquivo.getCaminho(),
                arquivo.getTipo(),
                arquivo.getMimeType(),
                arquivo.getTamanho(),
                arquivo.getHashArquivo(),
                arquivo.getDataUpload(),
                arquivo.getSubmissao().getId()
        );
    }

    private String sha256(byte[] bytes) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            return HexFormat.of()
                    .formatHex(digest.digest(bytes));

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(
                    "Não foi possível calcular hash do arquivo",
                    e
            );
        }
    }

    public record ArquivoDownload(
            Resource resource,
            String nomeArquivo,
            String mimeType
    ) {
    }
}
