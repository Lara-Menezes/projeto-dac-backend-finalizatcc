package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.model.Usuario;
import org.example.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class LegacyPasswordMigrationService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void migrateIfNecessary(String email, String rawPassword) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String storedPassword = usuario.getSenha();
            if (isBcrypt(storedPassword)) return;

            if (constantTimeEquals(storedPassword, rawPassword)) {
                usuario.setSenha(passwordEncoder.encode(rawPassword));
                usuarioRepository.save(usuario);
            }
        });
    }

    private boolean isBcrypt(String value) {
        return value != null && value.matches("^\\$2[ayb]\\$\\d{2}\\$.{53}$");
    }

    private boolean constantTimeEquals(String stored, String provided) {
        if (stored == null || provided == null) return false;
        return MessageDigest.isEqual(
                stored.getBytes(StandardCharsets.UTF_8),
                provided.getBytes(StandardCharsets.UTF_8));
    }
}
