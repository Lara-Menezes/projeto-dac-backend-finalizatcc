package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.StatusTcc;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TccUpdateRequestDTO {
    private String titulo;
    private String resumo;
    private Long areaId;
    private Long orientadorId;
    private Long coorientadorId;
    private StatusTcc status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
