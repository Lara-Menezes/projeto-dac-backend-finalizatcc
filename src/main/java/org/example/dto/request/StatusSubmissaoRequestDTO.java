package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.StatusSubmissao;

@Data
public class StatusSubmissaoRequestDTO {
    @NotNull
    private StatusSubmissao status;
}
