package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TipoUsuario;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private List<String> roles;

    public LoginResponseDTO(String token, Long id, String nome, String email, TipoUsuario tipo) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

}
