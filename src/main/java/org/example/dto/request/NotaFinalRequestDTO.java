package org.example.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotaFinalRequestDTO {
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double notaFinal;
}
