package org.example.entity;

import jakarta.persistence.*;
import org.example.enums.StatusTcc;

import java.time.LocalDate;

@Entity
@Table(name = "tccs")
public class Tcc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String resumo;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AreaPesquisa area;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "orientador_id")
    private Professor orientador;

    @ManyToOne
    @JoinColumn(name = "coorientador_id")
    private Professor coorientador;

    @Enumerated(EnumType.STRING)
    private StatusTcc status;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public AreaPesquisa getArea() {
        return area;
    }

    public void setArea(AreaPesquisa area) {
        this.area = area;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getOrientador() {
        return orientador;
    }

    public void setOrientador(Professor orientador) {
        this.orientador = orientador;
    }

    public Professor getCoorientador() {
        return coorientador;
    }

    public void setCoorientador(Professor coorientador) {
        this.coorientador = coorientador;
    }

    public StatusTcc getStatus() {
        return status;
    }

    public void setStatus(StatusTcc status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}
