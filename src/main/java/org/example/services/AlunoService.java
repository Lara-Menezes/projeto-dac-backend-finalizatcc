package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AlunoRequestDTO;
import org.example.dto.response.AlunoResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
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

    // Adicionar (Create)
    public AlunoResponseDTO save(AlunoRequestDTO request) {
        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha()) // Nota: em produção, criptografar senha
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

    // Atualizar
    public AlunoResponseDTO update(Long id, AlunoRequestDTO request) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Usuario usuario = aluno.getUsuario();

        // Atualizar Usuario
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        // Atualizar senha apenas se enviada
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(request.getSenha());
        }

        usuarioRepository.save(usuario);

        // Atualizar Aluno
        aluno.setMatricula(request.getMatricula());
        aluno.setCurso(request.getCurso());
        aluno.setPeriodo(request.getPeriodo());

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
