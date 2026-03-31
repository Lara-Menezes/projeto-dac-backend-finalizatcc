package org.example.entity;

import jakarta.persistence.*;
import org.example.enums.PapelAvaliador;

@Entity
@Table(name = "avaliadores")
public class Avaliador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PapelAvaliador papel;

    @ManyToOne
    @JoinColumn(name = "banca_id", nullable = false)
    private Banca banca;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PapelAvaliador getPapel() {
        return papel;
    }

    public void setPapel(PapelAvaliador papel) {
        this.papel = papel;
    }

    public Banca getBanca() {
        return banca;
    }

    public void setBanca(Banca banca) {
        this.banca = banca;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}