package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaResponseDTO {
    private Long id;
    private String acao;
    private String entidade;
    private Long entidadeId;
    private LocalDateTime data;
    private Long usuarioId;
}
