package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaRequestDTO {
    @NotBlank(message = "Ação é obrigatória")
    private String acao;

    @NotBlank(message = "Entidade é obrigatória")
    private String entidade;

    @NotNull(message = "ID da entidade é obrigatório")
    private Long entidadeId;

    private LocalDateTime data;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
}
