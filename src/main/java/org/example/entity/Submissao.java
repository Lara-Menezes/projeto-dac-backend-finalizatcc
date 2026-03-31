package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.example.enums.StatusSubmissao;

@Entity
@Table(name = "submissoes")
public class Submissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer versao;

    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    private StatusSubmissao status;

    private LocalDateTime prazoEntrega;

    @ManyToOne
    @JoinColumn(name = "tcc_id", nullable = false)
    private Tcc tcc;

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public StatusSubmissao getStatus() {
        return status;
    }

    public void setStatus(StatusSubmissao status) {
        this.status = status;
    }

    public LocalDateTime getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(LocalDateTime prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public Tcc getTcc() {
        return tcc;
    }

    public void setTcc(Tcc tcc) {
        this.tcc = tcc;
    }
}
