package org.example.services;


import lombok.RequiredArgsConstructor;
import org.example.dto.request.AlunoRequestDTO;
import org.example.dto.response.AlunoResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.Aluno;
import org.example.model.Usuario;
import org.example.repositories.AlunoRepository;
import org.example.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;

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

    // Excluir (Delete)
    public void deleteById(Long id) {
        alunoRepository.deleteById(id);
    }
}