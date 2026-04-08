package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Avaliador avaliador;
}