package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.TccRequestDTO;
import org.example.dto.response.TccResponseDTO;
import org.example.services.TccService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tccs")
@RequiredArgsConstructor
public class TccController {

    private final TccService tccService;

    // Criar TCC
    @PostMapping("/create")
    public ResponseEntity<TccResponseDTO> createTcc(@Valid @RequestBody TccRequestDTO request) {
        TccResponseDTO response = tccService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar TCCs
    @GetMapping
    public ResponseEntity<List<TccResponseDTO>> listAll() {

        List<TccResponseDTO> response = tccService.findAll();

        return ResponseEntity.ok(response);
    }

    // Atualizar TCC
    @PutMapping("/{id}")
    public ResponseEntity<TccResponseDTO> updateTcc(
            @PathVariable Long id,
            @Valid @RequestBody TccRequestDTO request) {

        TccResponseDTO response = tccService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar TCC
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTcc(@PathVariable Long id) {
        tccService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}