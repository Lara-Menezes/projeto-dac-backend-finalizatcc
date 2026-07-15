package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.services.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordenadores")
@PreAuthorize("hasRole('COORDENADOR')")
@RequiredArgsConstructor
public class CoordenadorController {
    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listAll() {
        return ResponseEntity.ok(professorService.findCoordenadores());
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> create(
            @Valid @RequestBody ProfessorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professorService.createCoordenador(request));
    }

    @PatchMapping("/professores/{professorId}/promover")
    public ResponseEntity<ProfessorResponseDTO> promote(@PathVariable Long professorId) {
        return ResponseEntity.ok(professorService.promoteToCoordenador(professorId));
    }
}
