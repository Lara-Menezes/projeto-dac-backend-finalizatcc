package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.TccRequestDTO;
import org.example.dto.response.TccResponseDTO;
import org.example.services.TccService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Deletar TCC
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTcc(@PathVariable Long id) {
        tccService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}