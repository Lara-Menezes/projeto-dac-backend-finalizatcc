package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(unique = true, nullable = false)
    private String matricula;

    private String curso;
    private Integer periodo;

}
