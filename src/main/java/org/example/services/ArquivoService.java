package org.example.services;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.example.dto.request.ArquivoRequestDTO;
import org.example.dto.response.ArquivoResponseDTO;
import org.example.model.Arquivo;
import org.example.model.Submissao;
import org.example.repositories.ArquivoRepository;
import org.example.repositories.SubmissaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;
    private final SubmissaoRepository submissaoRepository;

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
                .dataUpload(request.getDataUpload() != null ? request.getDataUpload() : LocalDateTime.now())
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
