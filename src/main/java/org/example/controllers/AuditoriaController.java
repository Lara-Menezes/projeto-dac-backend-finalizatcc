package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
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

    // Listar Auditorias
    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDTO>> listAll() {

        List<AuditoriaResponseDTO> response = auditoriaService.findAll();

        return ResponseEntity.ok(response);
    }

    // Buscar auditoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> findById(@PathVariable Long id) {

        AuditoriaResponseDTO response = auditoriaService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar Auditoria
    @PutMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> updateAuditoria(
            @PathVariable Long id,
            @Valid @RequestBody AuditoriaRequestDTO request) {

        AuditoriaResponseDTO response = auditoriaService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Auditoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditoria(@PathVariable Long id) {
        auditoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
