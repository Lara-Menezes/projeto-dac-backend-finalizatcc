package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.StatusTcc;

import java.time.LocalDate;

@Entity
@Table(name = "tccs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tcc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String resumo;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AreaPesquisa area;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "orientador_id", nullable = false)
    private Professor orientador;

    @ManyToOne
    @JoinColumn(name = "coorientador_id")
    private Professor coorientador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTcc status;

    private LocalDate dataInicio;
    private LocalDate dataFim;
}