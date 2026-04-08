package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.services.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    // Criar Professor
    @PostMapping("/create")
    public ResponseEntity<ProfessorResponseDTO> createProfessor(@Valid @RequestBody(required = true) ProfessorRequestDTO request) {
        ProfessorResponseDTO response = professorService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Deletar Professor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
