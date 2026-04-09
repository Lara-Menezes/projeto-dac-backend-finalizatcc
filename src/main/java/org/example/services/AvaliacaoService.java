package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliacaoRequestDTO;
import org.example.dto.response.AvaliacaoResponseDTO;
import org.example.model.Avaliacao;
import org.example.model.Avaliador;
import org.example.repositories.AvaliacaoRepository;
import org.example.repositories.AvaliadorRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AvaliadorRepository avaliadorRepository;

    public AvaliacaoResponseDTO save(AvaliacaoRequestDTO request) {
        Avaliador avaliador = avaliadorRepository.findById(request.getAvaliadorId())
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));

        Avaliacao avaliacao = Avaliacao.builder()
                .nota(request.getNota())
                .comentario(request.getComentario())
                .avaliador(avaliador)
                .build();

        avaliacao = avaliacaoRepository.save(avaliacao);

        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getAvaliador().getId()
        );
    }

    public void deleteById(Long id) {
        avaliacaoRepository.deleteById(id);
    }
}
