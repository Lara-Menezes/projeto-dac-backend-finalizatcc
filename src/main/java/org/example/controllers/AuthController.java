package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.LoginRequestDTO;
import org.example.dto.response.LoginResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.Usuario;
import org.example.repositories.AlunoRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.UsuarioRepository;
import org.example.services.JwtService;
import org.example.services.LegacyPasswordMigrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final JwtService jwtService;
    private final LegacyPasswordMigrationService passwordMigrationService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        passwordMigrationService.migrateIfNecessary(request.getEmail(), request.getSenha());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getSenha()
                        )
                );


        Usuario usuario = (Usuario) authentication.getPrincipal();


        String token = jwtService.gerarToken(usuario);
        Long perfilId = resolvePerfilId(usuario);


        LoginResponseDTO response =
                new LoginResponseDTO(
                        token,
                        perfilId,
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo()
                );
        response.setRoles(resolveRoles(usuario));

        return ResponseEntity.ok(response);
    }


    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO> me(
            Authentication authentication) {

        Usuario usuario = (Usuario) authentication.getPrincipal();
        Long perfilId = resolvePerfilId(usuario);


        LoginResponseDTO response =
                new LoginResponseDTO(
                        null,
                        perfilId,
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo()
                );
        response.setRoles(resolveRoles(usuario));


        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        /*
         * JWT é Stateless.
         *
         * Não existe sessão para invalidar.
         * O cliente deve remover o token salvo.
         */

        return ResponseEntity.noContent().build();
    }


    private Long resolvePerfilId(Usuario usuario) {


        if (usuario.getTipo() == TipoUsuario.ALUNO) {

            return alunoRepository.findByUsuarioId(usuario.getId())
                    .map(aluno -> aluno.getId())
                    .orElse(usuario.getId());

        }

        if (usuario.getTipo() == TipoUsuario.PROFESSOR
                || usuario.getTipo() == TipoUsuario.COORDENADOR) {


            return professorRepository.findByUsuarioId(usuario.getId())
                    .map(professor -> professor.getId())
                    .orElse(usuario.getId());

        }

        return usuario.getId();
    }

    private List<String> resolveRoles(Usuario usuario) {
        return usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replaceFirst("^ROLE_", ""))
                .toList();
    }

}
