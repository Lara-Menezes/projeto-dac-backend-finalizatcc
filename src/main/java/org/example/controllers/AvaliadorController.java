package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliadorRequestDTO;
import org.example.dto.response.AvaliadorResponseDTO;
import org.example.services.AvaliadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliadores")
@RequiredArgsConstructor
public class AvaliadorController {

    private final AvaliadorService avaliadorService;

    // Criar Avaliador
    @PostMapping("/create")
    public ResponseEntity<AvaliadorResponseDTO> createAvaliador(@Valid @RequestBody AvaliadorRequestDTO request) {
        AvaliadorResponseDTO response = avaliadorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Deletar Avaliador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliador(@PathVariable Long id) {
        avaliadorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
