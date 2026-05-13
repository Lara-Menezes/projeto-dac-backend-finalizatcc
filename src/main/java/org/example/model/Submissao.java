package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.example.enums.StatusSubmissao;

@Entity
@Table(name = "submissoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer versao;

    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSubmissao status;

    private LocalDateTime prazoEntrega;

    @ManyToOne
    @JoinColumn(name = "tcc_id", nullable = false)
    private Tcc tcc;
}