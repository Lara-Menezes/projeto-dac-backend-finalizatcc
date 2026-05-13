package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BancaResponseDTO {
    private Long id;
    private LocalDateTime data;
    private String local;
    private Double notaFinal;
    private Long tccId;
}
