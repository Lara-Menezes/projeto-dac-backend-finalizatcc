package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoUpdateRequestDTO {
    private String nome;
    private String curso;
    private Integer periodo;
}
