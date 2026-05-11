package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliacaoRequestDTO;
import org.example.dto.response.AvaliacaoResponseDTO;
import org.example.model.Avaliacao;
import org.example.model.Avaliador;
import org.example.repositories.AvaliacaoRepository;
import org.example.repositories.AvaliadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Listar
    public List<AvaliacaoResponseDTO> findAll() {

        List<Avaliacao> avaliacoes = avaliacaoRepository.findAll();

        return avaliacoes.stream()
                .map(avaliacao -> new AvaliacaoResponseDTO(
                        avaliacao.getId(),
                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        avaliacao.getAvaliador().getId()
                ))
                .toList();
    }

    // Buscar por ID
    public AvaliacaoResponseDTO findById(Long id) {

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Avaliação não encontrada"));

        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getAvaliador().getId()
        );
    }

    // Atualizar
    public AvaliacaoResponseDTO update(Long id, AvaliacaoRequestDTO request) {

        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Avaliador avaliador = avaliadorRepository.findById(request.getAvaliadorId())
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));

        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());
        avaliacao.setAvaliador(avaliador);

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
