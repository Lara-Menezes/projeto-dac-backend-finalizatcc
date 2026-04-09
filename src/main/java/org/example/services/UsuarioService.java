package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UsuarioRequestDTO;
import org.example.dto.response.UsuarioResponseDTO;
import org.example.model.Usuario;
import org.example.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO save(UsuarioRequestDTO request) {
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha()) // Nota: em produção, criptografar senha
                .tipo(request.getTipo())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .createdAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now())
                .build();

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.getAtivo(),
                usuario.getCreatedAt()
        );
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}