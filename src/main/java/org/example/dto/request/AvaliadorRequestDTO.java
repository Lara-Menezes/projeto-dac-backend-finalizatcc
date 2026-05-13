package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.PapelAvaliador;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliadorRequestDTO {
    @NotNull(message = "Papel é obrigatório")
    private PapelAvaliador papel;

    @NotNull(message = "ID da banca é obrigatório")
    private Long bancaId;

    @NotNull(message = "ID do professor é obrigatório")
    private Long professorId;
}
