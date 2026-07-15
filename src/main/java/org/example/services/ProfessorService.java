package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.request.ProfessorUpdateRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final BancaRepository bancaRepository;

    // Adicionar (Create)
    public ProfessorResponseDTO save(ProfessorRequestDTO request) {
        TipoUsuario tipoUsuario = Boolean.TRUE.equals(request.getCoordenador())
                || request.getTipo() == TipoUsuario.COORDENADOR
                ? TipoUsuario.COORDENADOR
                : TipoUsuario.PROFESSOR;

        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipo(tipoUsuario)
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

    // Buscar por email
    public ProfessorResponseDTO findByEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Professor professor = professorRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        return new ProfessorResponseDTO(
                professor.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                professor.getTitulacao(),
                professor.getAreaAtuacao()
        );
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

    // Atualizar por email
    public ProfessorResponseDTO updateByEmail(
            String email,
            ProfessorUpdateRequestDTO request) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Professor professor = professorRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        if (request.getNome() != null && !request.getNome().isBlank()) {
            usuario.setNome(request.getNome());
        }

        usuarioRepository.save(usuario);

        if (request.getAreaAtuacao() != null && !request.getAreaAtuacao().isBlank()) {
            professor.setAreaAtuacao(request.getAreaAtuacao());
        }

        if (request.getTitulacao() != null && !request.getTitulacao().isBlank()) {
            professor.setTitulacao(request.getTitulacao());
        }

        professor = professorRepository.save(professor);

        return new ProfessorResponseDTO(
                professor.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                professor.getAreaAtuacao(),
                professor.getTitulacao()
        );
    }

    // Atualizar por id
    public ProfessorResponseDTO update(Long id, ProfessorUpdateRequestDTO request) {

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Usuario usuario = professor.getUsuario();

        if (request.getNome() != null && !request.getNome().isBlank()) {
            usuario.setNome(request.getNome());
        }

        usuarioRepository.save(usuario);

        if (request.getAreaAtuacao() != null && !request.getAreaAtuacao().isBlank()) {
            professor.setAreaAtuacao(request.getAreaAtuacao());
        }
        if (request.getTitulacao() != null && !request.getTitulacao().isBlank()) {
            professor.setTitulacao(request.getTitulacao());
        }

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
