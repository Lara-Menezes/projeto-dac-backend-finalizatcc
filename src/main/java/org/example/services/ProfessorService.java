package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.Professor;
import org.example.model.Usuario;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;

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

    // Excluir (Delete)
    public void deleteById(Long id) {
        professorRepository.deleteById(id);
    }
}
