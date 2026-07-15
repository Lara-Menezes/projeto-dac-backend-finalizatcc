package org.example.services;

import org.example.enums.TipoUsuario;
import org.example.model.Professor;
import org.example.model.Usuario;
import org.example.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MultipleRolesTest {
    @Test
    void coordenadorAlsoHasProfessorRole() {
        Usuario usuario = Usuario.builder().tipo(TipoUsuario.COORDENADOR).ativo(true).build();
        Set<String> roles = usuario.getAuthorities().stream()
                .map(authority -> authority.getAuthority()).collect(Collectors.toSet());

        assertEquals(Set.of("ROLE_PROFESSOR", "ROLE_COORDENADOR"), roles);
    }

    @Test
    void promotingProfessorPreservesProfessorEntityAndAddsCoordinatorRole() {
        ProfessorRepository professores = mock(ProfessorRepository.class);
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        Usuario usuario = Usuario.builder().id(7L).tipo(TipoUsuario.PROFESSOR)
                .nome("Docente").email("docente@example.com").ativo(true).build();
        Professor professor = new Professor(7L, usuario, "Computação", "Mestre");
        when(professores.findById(7L)).thenReturn(java.util.Optional.of(professor));
        ProfessorService service = new ProfessorService(professores, usuarios,
                mock(AvaliadorRepository.class), mock(TccRepository.class),
                mock(PasswordEncoder.class), mock(BancaRepository.class));

        service.promoteToCoordenador(7L);

        assertEquals(TipoUsuario.COORDENADOR, usuario.getTipo());
        assertEquals(Set.of("ROLE_PROFESSOR", "ROLE_COORDENADOR"),
                usuario.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));
        verify(usuarios).save(usuario);
        verify(professores, never()).delete(any());
    }
}
