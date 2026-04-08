package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.example.enums.TipoArquivo;

@Entity
@Table(name = "arquivos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String caminho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoArquivo tipo;

    private String mimeType;
    private Long tamanho;
    private String hashArquivo;

    private LocalDateTime dataUpload;

    @ManyToOne
    @JoinColumn(name = "submissao_id", nullable = false)
    private Submissao submissao;
}