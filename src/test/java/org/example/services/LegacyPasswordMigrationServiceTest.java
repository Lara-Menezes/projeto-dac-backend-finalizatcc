package org.example.services;

import org.example.model.Usuario;
import org.example.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LegacyPasswordMigrationServiceTest {
    @Test
    void migratesMatchingPlainTextPasswordToBcrypt() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        Usuario usuario = Usuario.builder().senha("senhaAntiga123").build();
        when(repository.findByEmail("antigo@example.com")).thenReturn(Optional.of(usuario));
        when(encoder.encode("senhaAntiga123")).thenReturn("$2a$10$hashMigradoComTamanhoApenasParaTeste000000000000000");
        LegacyPasswordMigrationService service = new LegacyPasswordMigrationService(repository, encoder);

        service.migrateIfNecessary("antigo@example.com", "senhaAntiga123");

        assertEquals("$2a$10$hashMigradoComTamanhoApenasParaTeste000000000000000", usuario.getSenha());
        verify(repository).save(usuario);
    }

    @Test
    void doesNotMigrateWhenPasswordIsWrong() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        Usuario usuario = Usuario.builder().senha("senhaAntiga123").build();
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        LegacyPasswordMigrationService service = new LegacyPasswordMigrationService(repository, encoder);

        service.migrateIfNecessary("antigo@example.com", "senhaErrada");

        verify(repository, never()).save(any());
        verify(encoder, never()).encode(anyString());
    }
}
