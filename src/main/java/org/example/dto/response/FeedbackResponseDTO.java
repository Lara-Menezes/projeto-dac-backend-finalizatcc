package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponseDTO {
    private Long id;
    private String comentario;
    private Double nota;
    private LocalDateTime data;
    private Long submissaoId;
    private Long professorId;
    private String professorNome;

    public FeedbackResponseDTO(
            Long id,
            String comentario,
            Double nota,
            LocalDateTime data,
            Long submissaoId,
            Long professorId
    ) {
        this.id = id;
        this.comentario = comentario;
        this.nota = nota;
        this.data = data;
        this.submissaoId = submissaoId;
        this.professorId = professorId;
    }
}
