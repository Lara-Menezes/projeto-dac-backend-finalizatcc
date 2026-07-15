package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.example.dto.request.AlunoRequestDTO;
import org.example.dto.request.AlunoUpdateRequestDTO;
import org.example.dto.response.AlunoResponseDTO;
import org.example.services.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    // Criar Aluno
    @PostMapping("/create")
    public ResponseEntity<AlunoResponseDTO> createAluno(@Valid @RequestBody(required = true) AlunoRequestDTO request) {
        AlunoResponseDTO response = alunoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Alunos
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listAll() {

        List<AlunoResponseDTO> response = alunoService.findAll();

        return ResponseEntity.ok(response);
    }

    // Retorna os dados do aluno autenticado
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<AlunoResponseDTO> me(Authentication authentication) {

        return ResponseEntity.ok(
                alunoService.findByEmail(authentication.getName())
        );
    }

    // Atualiza os dados do aluno autenticado
    @PreAuthorize("hasRole('ALUNO')")
    @PutMapping("/me")
    public ResponseEntity<AlunoResponseDTO> updateMe(
            Authentication authentication,
            @RequestBody AlunoUpdateRequestDTO request) {

        return ResponseEntity.ok(
                alunoService.updateByEmail(
                        authentication.getName(),
                        request
                )
        );
    }

    // Buscar aluno por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> findById(@PathVariable Long id) {

        AlunoResponseDTO response = alunoService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar Aluno por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> updateAluno(
            @PathVariable Long id,
            @RequestBody AlunoUpdateRequestDTO request) {

        AlunoResponseDTO response = alunoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Aluno
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        alunoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
