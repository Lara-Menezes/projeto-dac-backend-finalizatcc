package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AvaliadorRequestDTO;
import org.example.dto.response.AvaliadorResponseDTO;
import org.example.services.AvaliadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliadores")
@PreAuthorize("hasRole('COORDENADOR')")
@RequiredArgsConstructor
public class AvaliadorController {

    private final AvaliadorService avaliadorService;

    // Criar Avaliador
    @PostMapping("/create")
    public ResponseEntity<AvaliadorResponseDTO> createAvaliador(@Valid @RequestBody AvaliadorRequestDTO request) {
        AvaliadorResponseDTO response = avaliadorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Avaliadores
    @GetMapping
    public ResponseEntity<List<AvaliadorResponseDTO>> listAll() {

        List<AvaliadorResponseDTO> response = avaliadorService.findAll();

        return ResponseEntity.ok(response);
    }

    // Buscar avaliador por ID
    @GetMapping("/{id}")
    public ResponseEntity<AvaliadorResponseDTO> findById(@PathVariable Long id) {

        AvaliadorResponseDTO response = avaliadorService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar avaliador
    @PutMapping("/{id}")
    public ResponseEntity<AvaliadorResponseDTO> updateAvaliador(
            @PathVariable Long id,
            @Valid @RequestBody AvaliadorRequestDTO request) {

        AvaliadorResponseDTO response = avaliadorService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Avaliador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliador(@PathVariable Long id) {
        avaliadorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
