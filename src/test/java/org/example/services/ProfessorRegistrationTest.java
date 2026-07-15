package org.example.services;

import org.example.dto.request.ProfessorRequestDTO;
import org.example.enums.TipoUsuario;
import org.example.model.Professor;
import org.example.model.Usuario;
import org.example.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfessorRegistrationTest {
    @Test
    void publicRegistrationCanNeverCreateCoordinator() {
        ProfessorRepository professorRepository = mock(ProfessorRepository.class);
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode(any())).thenReturn("hash");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(10L);
            return usuario;
        });
        when(professorRepository.save(any())).thenAnswer(invocation -> {
            Professor professor = invocation.getArgument(0);
            professor.setId(10L);
            return professor;
        });
        ProfessorService service = new ProfessorService(professorRepository, usuarioRepository,
                mock(AvaliadorRepository.class), mock(TccRepository.class), encoder,
                mock(BancaRepository.class));
        ProfessorRequestDTO request = new ProfessorRequestDTO(
                "Nome", "prof@example.com", "12345678", "Computação",
                "Mestre", TipoUsuario.COORDENADOR, true);

        service.register(request);

        assertEquals(TipoUsuario.PROFESSOR, request.getTipo());
        assertEquals(false, request.getCoordenador());
        verify(usuarioRepository).save(argThat(usuario -> usuario.getTipo() == TipoUsuario.PROFESSOR));
    }
}
