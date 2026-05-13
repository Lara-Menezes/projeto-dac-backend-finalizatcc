package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TipoUsuario;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private Boolean ativo;
    private LocalDateTime createdAt;
}
