package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ArquivoRequestDTO;
import org.example.dto.response.ArquivoResponseDTO;
import org.example.services.ArquivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;

    // Criar Arquivo
    @PostMapping("/create")
    public ResponseEntity<ArquivoResponseDTO> createArquivo(@Valid @RequestBody ArquivoRequestDTO request) {
        ArquivoResponseDTO response = arquivoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Deletar Arquivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable Long id) {
        arquivoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
