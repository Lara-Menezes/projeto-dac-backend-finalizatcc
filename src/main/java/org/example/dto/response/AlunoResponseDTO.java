package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private String curso;
    private Integer periodo;
}
