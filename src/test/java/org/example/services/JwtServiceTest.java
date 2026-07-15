package org.example.services;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.enums.TipoUsuario;
import org.example.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private static final String SECRET = "test-secret-with-at-least-thirty-two-bytes-123";

    @Test
    void generatesAndReadsAValidToken() {
        JwtService service = new JwtService(SECRET, 60_000);
        Usuario usuario = Usuario.builder().email("aluno@example.com")
                .tipo(TipoUsuario.ALUNO).ativo(true).build();

        String token = service.gerarToken(usuario);

        assertEquals("aluno@example.com", service.getEmail(token));
    }

    @Test
    void rejectsExpiredToken() throws InterruptedException {
        JwtService service = new JwtService(SECRET, 1);
        Usuario usuario = Usuario.builder().email("prof@example.com")
                .tipo(TipoUsuario.PROFESSOR).ativo(true).build();
        String token = service.gerarToken(usuario);
        Thread.sleep(10);

        assertThrows(ExpiredJwtException.class, () -> service.getEmail(token));
    }

    @Test
    void rejectsShortSecretAtStartup() {
        assertThrows(IllegalArgumentException.class, () -> new JwtService("short", 60_000));
    }
}
