package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.PapelAvaliador;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avaliadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PapelAvaliador papel;

    @ManyToOne
    @JoinColumn(name = "banca_id", nullable = false)
    private Banca banca;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @OneToMany(mappedBy = "avaliador", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<org.example.model.Avaliacao> avaliacoes = new ArrayList<>();
}