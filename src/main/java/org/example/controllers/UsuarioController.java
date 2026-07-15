package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.UsuarioRequestDTO;
import org.example.dto.response.UsuarioResponseDTO;
import org.example.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('COORDENADOR')")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Criar Usuário
    @PostMapping("/create")
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listAll() {

        List<UsuarioResponseDTO> response = usuarioService.findAll();

        return ResponseEntity.ok(response);
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {

        UsuarioResponseDTO response = usuarioService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO request) {

        UsuarioResponseDTO response = usuarioService.update(id, request);

        return ResponseEntity.ok(response);
    }


    // Deletar Usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // Reativa Usuário
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<String> reativar(@PathVariable Long id) {
        usuarioService.reativarUsuario(id);
        return ResponseEntity.ok("Usuário reativado com sucesso");
    }
}
