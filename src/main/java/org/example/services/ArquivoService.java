package org.example.services;

import lombok.RequiredArgsConstructor;
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

    public void deleteById(Long id) {
        arquivoRepository.deleteById(id);
    }
}
