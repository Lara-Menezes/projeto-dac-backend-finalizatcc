package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.PapelAvaliador;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliadorResponseDTO {
    private Long id;
    private PapelAvaliador papel;
    private Long bancaId;
    private Long professorId;
}
