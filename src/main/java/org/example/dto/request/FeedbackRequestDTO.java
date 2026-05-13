package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDTO {
    private String comentario;

    private Double nota;

    private LocalDateTime data;

    @NotNull(message = "ID da submissão é obrigatório")
    private Long submissaoId;

    @NotNull(message = "ID do professor é obrigatório")
    private Long professorId;
}
