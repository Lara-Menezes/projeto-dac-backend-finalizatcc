package org.example.services;

import jakarta.transaction.Transactional;
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

    // Listar
    public List<ProfessorResponseDTO> findAll() {

        List<Professor> professores = professorRepository.findAll();

        return professores.stream()
                .map(professor -> new ProfessorResponseDTO(
                        professor.getId(),
                        professor.getUsuario().getNome(),
                        professor.getUsuario().getEmail(),
                        professor.getAreaAtuacao(),
                        professor.getTitulacao()
                ))
                .toList();
    }

    // Buscar por ID
    public ProfessorResponseDTO findById(Long id) {

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Professor não encontrado"));

        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getUsuario().getNome(),
                professor.getUsuario().getEmail(),
                professor.getAreaAtuacao(),
                professor.getTitulacao()
        );
    }

    // Atualizar
    public ProfessorResponseDTO update(Long id, ProfessorRequestDTO request) {

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Usuario usuario = professor.getUsuario();

        // Atualizar Usuario
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        // Atualiza senha apenas se enviada
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(request.getSenha());
        }

        usuarioRepository.save(usuario);

        // Atualizar Professor
        professor.setAreaAtuacao(request.getAreaAtuacao());
        professor.setTitulacao(request.getTitulacao());

        professor = professorRepository.save(professor);

        return new ProfessorResponseDTO(
                professor.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                professor.getAreaAtuacao(),
                professor.getTitulacao()
        );
    }

    @Transactional
    public void deleteById(Long id) {

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Professor não encontrado"));

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        boolean orientador = tccRepository.existsByOrientadorId(id);
        boolean coorientador = tccRepository.existsByCoorientadorId(id);
        boolean avaliador = avaliadorRepository.existsByProfessorId(id);
        boolean possuiVinculo = orientador || coorientador || avaliador;

        // Se possue vinculo desativa
        if (possuiVinculo) {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);

            return;
        }
        // Se não tiver exclui
        professorRepository.deleteById(id);
        usuarioRepository.deleteById(id);
    }
}
