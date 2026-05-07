package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliacaoRequestDTO;
import org.example.dto.response.AvaliacaoResponseDTO;
import org.example.services.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Listar todas as avaliações
    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> listAll() {

        List<AvaliacaoResponseDTO> response = avaliacaoService.findAll();

        return ResponseEntity.ok(response);
    }

    // Atualizar avaliação
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> updateAvaliacao(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoRequestDTO request) {

        AvaliacaoResponseDTO response = avaliacaoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Avaliação
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable Long id) {
        avaliacaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
