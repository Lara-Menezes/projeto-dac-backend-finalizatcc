package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequestDTO {
    private Double nota;

    private String comentario;

    @NotNull(message = "ID do avaliador é obrigatório")
    private Long avaliadorId;
}
