package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private String entidade;

    @Column(nullable = false)
    private Long entidadeId;

    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}