package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.BancaRequestDTO;
import org.example.dto.response.BancaResponseDTO;
import org.example.services.BancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Deletar Banca
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanca(@PathVariable Long id) {
        bancaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
