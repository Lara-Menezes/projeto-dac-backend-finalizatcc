package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        return repository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Usuário não encontrado"
                        )
                );
    }

}

