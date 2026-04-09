package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AuditoriaRequestDTO;
import org.example.dto.response.AuditoriaResponseDTO;
import org.example.services.AuditoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // Criar Auditoria
    @PostMapping("/create")
    public ResponseEntity<AuditoriaResponseDTO> createAuditoria(@Valid @RequestBody AuditoriaRequestDTO request) {
        AuditoriaResponseDTO response = auditoriaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Deletar Auditoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditoria(@PathVariable Long id) {
        auditoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
