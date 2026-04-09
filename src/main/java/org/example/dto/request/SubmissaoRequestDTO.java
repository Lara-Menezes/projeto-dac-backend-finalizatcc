package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.StatusSubmissao;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissaoRequestDTO {
    @NotNull(message = "Versão é obrigatória")
    private Integer versao;

    private LocalDateTime dataEnvio;

    @NotNull(message = "Status é obrigatório")
    private StatusSubmissao status;

    private LocalDateTime prazoEntrega;

    @NotNull(message = "ID do TCC é obrigatório")
    private Long tccId;
}
