package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.LoginRequestDTO;
import org.example.dto.response.LoginResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.Usuario;
import org.example.repositories.AlunoRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public static final String AUTHENTICATED_USER = "authenticatedUser";

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos"));

        if (!usuario.getSenha().equals(request.getSenha()) || Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos");
        }

        Long perfilId = resolvePerfilId(usuario);

        LoginResponseDTO response = new LoginResponseDTO(
                perfilId,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo()
        );

        HttpSession currentSession = httpRequest.getSession(false);
        if (currentSession != null) {
            currentSession.invalidate();
        }

        HttpSession newSession = httpRequest.getSession(true);
        newSession.setAttribute(AUTHENTICATED_USER, response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO> me(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessao nao encontrada");
        }

        Object authenticatedUser = session.getAttribute(AUTHENTICATED_USER);
        if (!(authenticatedUser instanceof LoginResponseDTO user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessao invalida");
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }

    private Long resolvePerfilId(Usuario usuario) {
        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            return alunoRepository.findByUsuarioId(usuario.getId())
                    .map(aluno -> aluno.getId())
                    .orElse(usuario.getId());
        }

        if (usuario.getTipo() == TipoUsuario.PROFESSOR || usuario.getTipo() == TipoUsuario.COORDENADOR) {
            return professorRepository.findByUsuarioId(usuario.getId())
                    .map(professor -> professor.getId())
                    .orElse(usuario.getId());
        }

        return usuario.getId();
    }
}
