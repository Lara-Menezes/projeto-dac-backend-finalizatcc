package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvaliadorRepository avaliadorRepository;
    private final TccRepository tccRepository;
    private final BancaRepository bancaRepository;

    // Adicionar (Create)
    public ProfessorResponseDTO save(ProfessorRequestDTO request) {
        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha()) // Nota: em produção, criptografar senha
                .tipo(TipoUsuario.PROFESSOR)
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .build();
        usuario = usuarioRepository.save(usuario);

        // Criar Professor
        Professor professor = new Professor();
        professor.setUsuario(usuario);
        professor.setAreaAtuacao(request.getAreaAtuacao());
        professor.setTitulacao(request.getTitulacao());
        professor = professorRepository.save(professor);

        // Retornar DTO
        return new ProfessorResponseDTO(
                professor.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                professor.getAreaAtuacao(),
                professor.getTitulacao()
        );
    }

    // Excluir (Delete) com cascata
    public void deleteById(Long id) {
        // Buscar Tccs onde o professor é orientador ou coorientador
        List<Tcc> tccsOrientador = tccRepository.findByOrientadorId(id);
        List<Tcc> tccsCoorientador = tccRepository.findByCoorientadorId(id);
        List<Tcc> tccs = Stream.concat(tccsOrientador.stream(), tccsCoorientador.stream())
                .distinct()
                .collect(Collectors.toList());

        // Para cada Tcc, deletar dependentes: Avaliadores da Banca, depois Banca, depois Tcc
        for (Tcc tcc : tccs) {
            Banca banca = bancaRepository.findByTccId(tcc.getId()).orElse(null);
            if (banca != null) {
                List<Avaliador> avaliadoresBanca = avaliadorRepository.findByBancaId(banca.getId());
                for (Avaliador av : avaliadoresBanca) {
                    avaliadorRepository.delete(av);
                }
                bancaRepository.delete(banca);
            }
            tccRepository.delete(tcc);
        }

        // Deletar Avaliadores diretos do professor
        List<Avaliador> avaliadoresProf = avaliadorRepository.findByProfessorId(id);
        for (Avaliador av : avaliadoresProf) {
            avaliadorRepository.delete(av);
        }

        // Deletar o Professor
        professorRepository.deleteById(id);

        // **IMPORTANTE: Deletar o Usuario associado**
        usuarioRepository.deleteById(id);
    }
}
