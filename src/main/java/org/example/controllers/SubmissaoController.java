package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.SubmissaoRequestDTO;
import org.example.dto.response.SubmissaoResponseDTO;
import org.example.services.SubmissaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissoes")
@RequiredArgsConstructor
public class SubmissaoController {

    private final SubmissaoService submissaoService;

    // Criar Submissão
    @PostMapping("/create")
    public ResponseEntity<SubmissaoResponseDTO> createSubmissao(@Valid @RequestBody SubmissaoRequestDTO request) {
        SubmissaoResponseDTO response = submissaoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar submissões
    @GetMapping
    public ResponseEntity<List<SubmissaoResponseDTO>> listAll() {

        List<SubmissaoResponseDTO> response = submissaoService.findAll();

        return ResponseEntity.ok(response);
    }

    // Buscar submissão por ID
    @GetMapping("/{id}")
    public ResponseEntity<SubmissaoResponseDTO> findById(@PathVariable Long id) {

        SubmissaoResponseDTO response = submissaoService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar submissão
    @PutMapping("/{id}")
    public ResponseEntity<SubmissaoResponseDTO> updateSubmissao(
            @PathVariable Long id,
            @Valid @RequestBody SubmissaoRequestDTO request) {

        SubmissaoResponseDTO response = submissaoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Submissão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmissao(@PathVariable Long id) {
        submissaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
