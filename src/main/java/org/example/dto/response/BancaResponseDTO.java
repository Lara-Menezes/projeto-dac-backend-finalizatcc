package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BancaResponseDTO {
    private Long id;
    private LocalDateTime data;
    private String local;
    private Double notaFinal;
    private Long tccId;
    private String tccTitulo;
    private Long alunoId;
    private String alunoNome;
    private Long orientadorId;
    private String orientadorNome;
    private List<AvaliadorResumoDTO> avaliadores;

    public BancaResponseDTO(Long id, LocalDateTime data, String local, Double notaFinal, Long tccId) {
        this.id = id;
        this.data = data;
        this.local = local;
        this.notaFinal = notaFinal;
        this.tccId = tccId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvaliadorResumoDTO {
        private Long id;
        private Long professorId;
        private String professorNome;
        private String papel;
    }
}
