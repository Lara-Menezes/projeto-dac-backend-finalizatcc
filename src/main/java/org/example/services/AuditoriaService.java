package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AuditoriaRequestDTO;
import org.example.dto.response.AuditoriaResponseDTO;
import org.example.model.Auditoria;
import org.example.model.Usuario;
import org.example.repositories.AuditoriaRepository;
import org.example.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public AuditoriaResponseDTO save(AuditoriaRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Auditoria auditoria = Auditoria.builder()
                .acao(request.getAcao())
                .entidade(request.getEntidade())
                .entidadeId(request.getEntidadeId())
                .data(request.getData() != null ? request.getData() : LocalDateTime.now())
                .usuario(usuario)
                .build();

        auditoria = auditoriaRepository.save(auditoria);

        return new AuditoriaResponseDTO(
                auditoria.getId(),
                auditoria.getAcao(),
                auditoria.getEntidade(),
                auditoria.getEntidadeId(),
                auditoria.getData(),
                auditoria.getUsuario().getId()
        );
    }

    public void deleteById(Long id) {
        auditoriaRepository.deleteById(id);
    }
}
