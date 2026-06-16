package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.StatusTcc;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TccResponseDTO {
    private Long id;
    private String titulo;
    private String resumo;
    private Long areaId;
    private Long alunoId;
    private Long orientadorId;
    private Long coorientadorId;
    private StatusTcc status;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String areaNome;
    private String alunoNome;
    private String alunoEmail;
    private String orientadorNome;
    private String orientadorEmail;
    private String coorientadorNome;
    private String coorientadorEmail;

    public TccResponseDTO(
            Long id,
            String titulo,
            String resumo,
            Long areaId,
            Long alunoId,
            Long orientadorId,
            Long coorientadorId,
            StatusTcc status,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        this.id = id;
        this.titulo = titulo;
        this.resumo = resumo;
        this.areaId = areaId;
        this.alunoId = alunoId;
        this.orientadorId = orientadorId;
        this.coorientadorId = coorientadorId;
        this.status = status;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
}
