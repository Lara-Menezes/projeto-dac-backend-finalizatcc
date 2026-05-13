package org.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "Matrícula é obrigatória")
    private String matricula;

    @NotBlank(message = "Curso é obrigatório")
    private String curso;

    @NotNull(message = "Período é obrigatório")
    private Integer periodo;
}
