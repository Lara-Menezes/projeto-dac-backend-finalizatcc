package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.BancaRequestDTO;
import org.example.dto.response.BancaResponseDTO;
import org.example.services.BancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bancas")
@RequiredArgsConstructor
public class BancaController {

    private final BancaService bancaService;

    // Criar Banca
    @PostMapping("/create")
    public ResponseEntity<BancaResponseDTO> createBanca(@Valid @RequestBody BancaRequestDTO request) {
        BancaResponseDTO response = bancaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Bancas
    @GetMapping
    public ResponseEntity<List<BancaResponseDTO>> listAll() {

        List<BancaResponseDTO> response = bancaService.findAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tcc/{tccId}")
    public ResponseEntity<BancaResponseDTO> findByTcc(@PathVariable Long tccId) {
        BancaResponseDTO response = bancaService.findByTccId(tccId);
        return ResponseEntity.ok(response);
    }

    // Buscar banca por ID
    @GetMapping("/{id}")
    public ResponseEntity<BancaResponseDTO> findById(@PathVariable Long id) {

        BancaResponseDTO response = bancaService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar banca
    @PutMapping("/{id}")
    public ResponseEntity<BancaResponseDTO> updateBanca(
            @PathVariable Long id,
            @Valid @RequestBody BancaRequestDTO request) {

        BancaResponseDTO response = bancaService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Banca
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanca(@PathVariable Long id) {
        bancaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
