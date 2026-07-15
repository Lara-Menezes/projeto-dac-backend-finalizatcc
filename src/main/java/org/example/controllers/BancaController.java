package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.BancaRequestDTO;
import org.example.dto.request.NotaFinalRequestDTO;
import org.example.dto.response.BancaResponseDTO;
import org.example.services.BancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bancas")
@RequiredArgsConstructor
public class BancaController {

    private final BancaService bancaService;

    // Criar Banca
    @PreAuthorize("hasRole('COORDENADOR')")
    @PostMapping("/create")
    public ResponseEntity<BancaResponseDTO> createBanca(@Valid @RequestBody BancaRequestDTO request) {
        BancaResponseDTO response = bancaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Bancas
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<BancaResponseDTO>> listAll() {

        List<BancaResponseDTO> response = bancaService.findAll();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<List<BancaResponseDTO>> minhasBancas(Authentication authentication) {
        return ResponseEntity.ok(bancaService.findForAluno(authentication.getName()));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/me")
    public ResponseEntity<List<BancaResponseDTO>> bancasDoProfessor(Authentication authentication) {
        return ResponseEntity.ok(bancaService.findForProfessor(authentication.getName()));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @PatchMapping("/{id}/nota-final")
    public ResponseEntity<BancaResponseDTO> registrarNotaFinal(
            @PathVariable Long id, Authentication authentication,
            @Valid @RequestBody NotaFinalRequestDTO request) {
        return ResponseEntity.ok(bancaService.updateGradeForProfessor(
                id, authentication.getName(), request.getNotaFinal()));
    }

    @GetMapping("/tcc/{tccId}")
    @PreAuthorize("hasRole('COORDENADOR')")
    public ResponseEntity<BancaResponseDTO> findByTcc(@PathVariable Long tccId) {
        BancaResponseDTO response = bancaService.findByTccId(tccId);
        return ResponseEntity.ok(response);
    }

    // Buscar banca por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<BancaResponseDTO> findById(@PathVariable Long id) {

        BancaResponseDTO response = bancaService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar banca
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<BancaResponseDTO> updateBanca(
            @PathVariable Long id,
            @Valid @RequestBody BancaRequestDTO request) {

        BancaResponseDTO response = bancaService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Banca
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanca(@PathVariable Long id) {
        bancaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
