package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliacaoRequestDTO;
import org.example.dto.response.AvaliacaoResponseDTO;
import org.example.services.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    // Criar Avaliação
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("/create")
    public ResponseEntity<AvaliacaoResponseDTO> createAvaliacao(Authentication authentication,
                                                                 @Valid @RequestBody AvaliacaoRequestDTO request) {
        AvaliacaoResponseDTO response = avaliacaoService.saveForProfessor(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar todas as avaliações
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listAll() {

        List<AvaliacaoResponseDTO> response = avaliacaoService.findAll();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<List<AvaliacaoResponseDTO>> minhasAvaliacoes(Authentication authentication) {
        return ResponseEntity.ok(avaliacaoService.findForAluno(authentication.getName()));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/me")
    public ResponseEntity<List<AvaliacaoResponseDTO>> avaliacoesDoProfessor(Authentication authentication) {
        return ResponseEntity.ok(avaliacaoService.findForProfessor(authentication.getName()));
    }

    // Buscar avaliação por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> findById(@PathVariable Long id) {

        AvaliacaoResponseDTO response = avaliacaoService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar avaliação
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> updateAvaliacao(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoRequestDTO request) {

        AvaliacaoResponseDTO response = avaliacaoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Avaliação
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable Long id) {
        avaliacaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
