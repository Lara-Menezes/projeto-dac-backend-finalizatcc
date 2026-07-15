package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AlunoRequestDTO;
import org.example.dto.request.AlunoUpdateRequestDTO;
import org.example.dto.response.AlunoResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvaliadorRepository avaliadorRepository;
    private final TccRepository tccRepository;
    private final BancaRepository bancaRepository;
    private final PasswordEncoder passwordEncoder;

    // Adicionar (Create)
    public AlunoResponseDTO save(AlunoRequestDTO request) {
        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipo(TipoUsuario.ALUNO)
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .build();
        usuario = usuarioRepository.save(usuario);

        // Criar Aluno
        Aluno aluno = new Aluno();
        aluno.setUsuario(usuario);
        aluno.setMatricula(request.getMatricula());
        aluno.setCurso(request.getCurso());
        aluno.setPeriodo(request.getPeriodo());
        aluno = alunoRepository.save(aluno);

        // Retornar DTO
        return new AlunoResponseDTO(
                aluno.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getPeriodo()
        );
    }

    // Listar todos
    public List<AlunoResponseDTO> findAll() {

        List<Aluno> alunos = alunoRepository.findAll();

        return alunos.stream()
                .map(aluno -> new AlunoResponseDTO(
                        aluno.getId(),
                        aluno.getUsuario().getNome(),
                        aluno.getUsuario().getEmail(),
                        aluno.getMatricula(),
                        aluno.getCurso(),
                        aluno.getPeriodo()
                ))
                .toList();
    }

    // Busca por email
    public AlunoResponseDTO findByEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        Aluno aluno = alunoRepository.findById(usuario.getId())
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado")
                );

        return new AlunoResponseDTO(
                aluno.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getPeriodo()
        );
    }

    // Buscar por ID
    public AlunoResponseDTO findById(Long id) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getUsuario().getNome(),
                aluno.getUsuario().getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getPeriodo()
        );
    }


    // Atualizar por email
    public AlunoResponseDTO updateByEmail(
            String email,
            AlunoUpdateRequestDTO request) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        Aluno aluno = alunoRepository.findById(usuario.getId())
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        if (request.getNome() != null && !request.getNome().isBlank()) {
            usuario.setNome(request.getNome());
        }

        usuarioRepository.save(usuario);

        if (request.getCurso() != null && !request.getCurso().isBlank()) {
            aluno.setCurso(request.getCurso());
        }

        if (request.getPeriodo() != null) {
            aluno.setPeriodo(request.getPeriodo());
        }

        aluno = alunoRepository.save(aluno);

        return new AlunoResponseDTO(
                aluno.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getPeriodo()
        );
    }

    // Atualizar por id
    public AlunoResponseDTO update(Long id, AlunoUpdateRequestDTO request) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario usuario = aluno.getUsuario();

        if (request.getNome() != null && !request.getNome().isBlank()) {
            usuario.setNome(request.getNome());
        }

        usuarioRepository.save(usuario);

        if (request.getCurso() != null && !request.getCurso().isBlank()) {
            aluno.setCurso(request.getCurso());
        }
        if (request.getPeriodo() != null) {
            aluno.setPeriodo(request.getPeriodo());
        }

        aluno = alunoRepository.save(aluno);

        return new AlunoResponseDTO(
                aluno.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getPeriodo()
        );
    }

    @Transactional
    public void deleteById(Long id) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        boolean possuiTcc = tccRepository.existsByAlunoId(id);

        // Se possue vinculo desativa
        if (possuiTcc) {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);

            return;
        }
        // Se não tiver exclui
        alunoRepository.deleteById(id);
        usuarioRepository.deleteById(id);
    }
}
