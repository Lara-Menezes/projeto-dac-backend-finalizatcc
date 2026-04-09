package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.StatusTcc;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TccRequestDTO {
    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    private String resumo;

    private Long areaId;

    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "ID do orientador é obrigatório")
    private Long orientadorId;

    private Long coorientadorId;

    @NotNull(message = "Status é obrigatório")
    private StatusTcc status;

    private LocalDate dataInicio;
    private LocalDate dataFim;
}
