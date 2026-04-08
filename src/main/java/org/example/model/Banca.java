package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bancas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    private String local;

    private Double notaFinal;

    @ManyToOne
    @JoinColumn(name = "tcc_id", nullable = false)
    private Tcc tcc;
}