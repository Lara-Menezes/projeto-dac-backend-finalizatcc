package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliacaoRequestDTO;
import org.example.dto.response.AvaliacaoResponseDTO;
import org.example.services.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    // Criar Avaliação
    @PostMapping("/create")
    public ResponseEntity<AvaliacaoResponseDTO> createAvaliacao(@Valid @RequestBody AvaliacaoRequestDTO request) {
        AvaliacaoResponseDTO response = avaliacaoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Deletar Avaliação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable Long id) {
        avaliacaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
