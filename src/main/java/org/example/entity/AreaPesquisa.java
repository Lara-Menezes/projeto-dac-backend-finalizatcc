package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "areas_pesquisa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreaPesquisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
}
