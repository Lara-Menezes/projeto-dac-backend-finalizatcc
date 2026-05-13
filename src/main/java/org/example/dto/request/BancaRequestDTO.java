package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BancaRequestDTO {
    private LocalDateTime data;

    private String local;

    private Double notaFinal;

    @NotNull(message = "ID do TCC é obrigatório")
    private Long tccId;
}
