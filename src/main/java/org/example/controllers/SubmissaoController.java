package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.SubmissaoRequestDTO;
import org.example.dto.response.SubmissaoResponseDTO;
import org.example.services.SubmissaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Deletar Submissão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmissao(@PathVariable Long id) {
        submissaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
