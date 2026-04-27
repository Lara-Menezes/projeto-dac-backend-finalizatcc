package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.model.Usuario;
import org.example.repositories.UsuarioRepository;
// IMPORTANTE: Mude 'TipoUsuario' para o nome do Enum que você usa na sua classe Usuario
import org.example.enums.TipoUsuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String emailCoordenador = "coordenador@ifpb.edu.br";

        // Verifica se o coordenador já existe para não criar duplicado toda vez que reiniciar
        if (usuarioRepository.findByEmail(emailCoordenador).isEmpty()) {

            Usuario coordenador = Usuario.builder()
                    .nome("Super Coordenador")
                    .email(emailCoordenador)
                    // A senha PRECISA ser criptografada para o login funcionar depois!
                    .senha(passwordEncoder.encode("SenhaForte123"))
                    .tipo(TipoUsuario.COORDENADOR) // <- Ajuste aqui para o nome exato do seu Enum
                    .ativo(true)
                    .build();

            usuarioRepository.save(coordenador);
            System.out.println("🌱 [DataSeeder] - Coordenador Master criado com sucesso!");
        } else {
            System.out.println("✅ [DataSeeder] - Coordenador Master já existe no banco.");
        }
    }
}
