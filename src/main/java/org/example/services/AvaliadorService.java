package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliadorRequestDTO;
import org.example.dto.response.AvaliadorResponseDTO;
import org.example.model.Avaliador;
import org.example.model.Banca;
import org.example.model.Professor;
import org.example.repositories.AvaliadorRepository;
import org.example.repositories.BancaRepository;
import org.example.repositories.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorService {

    private final AvaliadorRepository avaliadorRepository;
    private final BancaRepository bancaRepository;
    private final ProfessorRepository professorRepository;

    public AvaliadorResponseDTO save(AvaliadorRequestDTO request) {
        Banca banca = bancaRepository.findById(request.getBancaId())
                .orElseThrow(() -> new RuntimeException("Banca não encontrada"));
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Avaliador avaliador = Avaliador.builder()
                .papel(request.getPapel())
                .banca(banca)
                .professor(professor)
                .build();

        avaliador = avaliadorRepository.save(avaliador);

        return new AvaliadorResponseDTO(
                avaliador.getId(),
                avaliador.getPapel(),
                avaliador.getBanca().getId(),
                avaliador.getProfessor().getId()
        );
    }

    public void deleteById(Long id) {
        avaliadorRepository.deleteById(id);
    }
}
