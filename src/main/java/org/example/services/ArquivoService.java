package org.example.services;

import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;
import java.util.List;
import org.example.dto.request.ArquivoRequestDTO;
import org.example.dto.response.ArquivoResponseDTO;
import org.example.enums.TipoArquivo;
import org.example.model.Arquivo;
import org.example.model.Submissao;
import org.example.repositories.ArquivoRepository;
import org.example.repositories.SubmissaoRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;
    private final SubmissaoRepository submissaoRepository;

    //Upload
    public ArquivoResponseDTO upload(
            MultipartFile file,
            Long submissaoId,
            TipoArquivo tipo
    ) throws IOException {

        Submissao submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() ->
                        new RuntimeException("Submissão não encontrada"));

        // Valida PDF
        if (!"application/pdf".equals(file.getContentType())) {
            throw new RuntimeException("Somente PDF é permitido");
        }

        // Gera nome único
        String nomeArquivo =
                UUID.randomUUID() + "_" +
                        file.getOriginalFilename();

        // Cria pasta uploads
        Path uploadPath = Paths.get("uploads");

        Files.createDirectories(uploadPath);

        // Caminho final
        Path filePath = uploadPath.resolve(nomeArquivo);

        // Salvar arquivo no disco
        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // Criar entidade Arquivo
        Arquivo arquivo = Arquivo.builder()
                .nomeArquivo(file.getOriginalFilename())
                .caminho(filePath.toString())
                .tipo(tipo)
                .mimeType(file.getContentType())
                .tamanho(file.getSize())
                .dataUpload(LocalDateTime.now())
                .submissao(submissao)
                .build();

        arquivo = arquivoRepository.save(arquivo);

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

    //Dowload
    public Resource downloadArquivo(Long id)
            throws MalformedURLException {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Arquivo não encontrado"));

        Path path = Paths.get(arquivo.getCaminho());

        Resource resource =
                new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("Arquivo não encontrado no disco");
        }

        return resource;
    }

    // Listar
    public List<ArquivoResponseDTO> findAll() {

        List<Arquivo> arquivos = arquivoRepository.findAll();

        return arquivos.stream()
                .map(arquivo -> new ArquivoResponseDTO(
                        arquivo.getId(),
                        arquivo.getNomeArquivo(),
                        arquivo.getCaminho(),
                        arquivo.getTipo(),
                        arquivo.getMimeType(),
                        arquivo.getTamanho(),
                        arquivo.getHashArquivo(),
                        arquivo.getDataUpload(),
                        arquivo.getSubmissao().getId()
                ))
                .toList();
    }

    // Buscar por ID
    public ArquivoResponseDTO findById(Long id) {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Arquivo não encontrado"));

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

    // Atualizar
    public ArquivoResponseDTO update(Long id, ArquivoRequestDTO request) {

        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        Submissao submissao = submissaoRepository.findById(request.getSubmissaoId())
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));

        arquivo.setNomeArquivo(request.getNomeArquivo());
        arquivo.setCaminho(request.getCaminho());
        arquivo.setTipo(request.getTipo());
        arquivo.setMimeType(request.getMimeType());
        arquivo.setTamanho(request.getTamanho());
        arquivo.setHashArquivo(request.getHashArquivo());
        arquivo.setSubmissao(submissao);

        // Mantém a data atual se vier null
        arquivo.setDataUpload(
                request.getDataUpload() != null
                        ? request.getDataUpload()
                        : arquivo.getDataUpload()
        );

        arquivo = arquivoRepository.save(arquivo);

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

    public void deleteById(Long id) {
        arquivoRepository.deleteById(id);
    }
}
