package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.SubmissaoRequestDTO;
import org.example.dto.response.SubmissaoResponseDTO;
import org.example.services.SubmissaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissoes")
@RequiredArgsConstructor
public class SubmissaoController {

    private final SubmissaoService submissaoService;

    // Criar Submissão
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/create")
    public ResponseEntity<SubmissaoResponseDTO> createSubmissao(@Valid @RequestBody SubmissaoRequestDTO request) {
        SubmissaoResponseDTO response = submissaoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar submissões
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<SubmissaoResponseDTO>> listAll() {

        List<SubmissaoResponseDTO> response = submissaoService.findAll();

        return ResponseEntity.ok(response);
    }

    // Listar submissões para Aluno
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<List<SubmissaoResponseDTO>> minhasSubmissoes(
            Authentication authentication) {

        return ResponseEntity.ok(
                submissaoService.findByAlunoEmail(authentication.getName())
        );
    }

    // Ver submissões dos TCCs que orienta
    @PreAuthorize("hasAnyRole('PROFESSOR','COORDENADOR')")
    @GetMapping("/professor")
    public ResponseEntity<List<SubmissaoResponseDTO>> submissoesProfessor(
            Authentication authentication) {

        return ResponseEntity.ok(
                submissaoService.findByProfessorEmail(authentication.getName())
        );
    }

    // Buscar submissão por TCC ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/tcc/{tccId}")
    public ResponseEntity<List<SubmissaoResponseDTO>> findByTcc(@PathVariable Long tccId) {
        List<SubmissaoResponseDTO> response = submissaoService.findByTccId(tccId);
        return ResponseEntity.ok(response);
    }

    // Buscar submissão por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SubmissaoResponseDTO> findById(@PathVariable Long id) {

        SubmissaoResponseDTO response = submissaoService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar submissão
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<SubmissaoResponseDTO> updateSubmissao(
            @PathVariable Long id,
            @Valid @RequestBody SubmissaoRequestDTO request) {

        SubmissaoResponseDTO response = submissaoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Submissão
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmissao(@PathVariable Long id) {
        submissaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
