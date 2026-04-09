package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.StatusSubmissao;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissaoResponseDTO {
    private Long id;
    private Integer versao;
    private LocalDateTime dataEnvio;
    private StatusSubmissao status;
    private LocalDateTime prazoEntrega;
    private Long tccId;
}
