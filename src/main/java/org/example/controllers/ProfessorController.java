package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.request.ProfessorUpdateRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.services.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    // Criar Professor
    @PreAuthorize("hasRole('COORDENADOR')")
    @PostMapping("/create")
    public ResponseEntity<ProfessorResponseDTO> createProfessor(@Valid @RequestBody(required = true) ProfessorRequestDTO request) {
        ProfessorResponseDTO response = professorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar professores
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listAll() {

        List<ProfessorResponseDTO> response = professorService.findAll();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ProfessorResponseDTO> registerProfessor(
            @Valid @RequestBody ProfessorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.register(request));
    }

    @PreAuthorize("hasAnyRole('ALUNO','COORDENADOR')")
    @GetMapping("/orientadores")
    public ResponseEntity<List<ProfessorResponseDTO>> listOrientadores() {
        return ResponseEntity.ok(professorService.findAll());
    }

    // Retorna os dados do professor autenticado
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    @GetMapping("/me")
    public ResponseEntity<ProfessorResponseDTO> me(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                professorService.findByEmail(email)
        );
    }

    // Atualiza os dados do professor autenticado
    @PreAuthorize("hasRole('PROFESSOR')")
    @PutMapping("/me")
    public ResponseEntity<ProfessorResponseDTO> updateMe(
            Authentication authentication,
            @RequestBody ProfessorUpdateRequestDTO request) {

        return ResponseEntity.ok(
                professorService.updateByEmail(authentication.getName(), request)
        );
    }


    // Buscar professor por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> findById(@PathVariable Long id) {

        ProfessorResponseDTO response = professorService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar professor por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> updateProfessor(
            @PathVariable Long id,
            @RequestBody ProfessorUpdateRequestDTO request) {

        ProfessorResponseDTO response = professorService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Professor
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
