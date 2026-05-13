package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponseDTO {
    private Long id;
    private Double nota;
    private String comentario;
    private Long avaliadorId;
}
