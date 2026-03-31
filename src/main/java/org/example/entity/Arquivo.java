package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.example.enums.TipoArquivo;

@Entity
@Table(name = "arquivos")
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String caminho;

    @Enumerated(EnumType.STRING)
    private TipoArquivo tipo;

    private String mimeType;
    private Long tamanho;
    private String hashArquivo;

    private LocalDateTime dataUpload;

    @ManyToOne
    @JoinColumn(name = "submissao_id", nullable = false)
    private Submissao submissao;

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public TipoArquivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoArquivo tipo) {
        this.tipo = tipo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public String getHashArquivo() {
        return hashArquivo;
    }

    public void setHashArquivo(String hashArquivo) {
        this.hashArquivo = hashArquivo;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public Submissao getSubmissao() {
        return submissao;
    }

    public void setSubmissao(Submissao submissao) {
        this.submissao = submissao;
    }
}
