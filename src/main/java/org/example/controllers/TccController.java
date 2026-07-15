package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.TccRequestDTO;
import org.example.dto.response.TccResponseDTO;
import org.example.services.TccService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tccs")
@RequiredArgsConstructor
public class TccController {

    private final TccService tccService;

    // Criar TCC
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/create")
    public ResponseEntity<TccResponseDTO> createTcc(Authentication authentication,
                                                     @Valid @RequestBody TccRequestDTO request) {
        TccResponseDTO response = tccService.saveForAluno(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar TCCs
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<TccResponseDTO>> listAll() {

        List<TccResponseDTO> response = tccService.findAll();

        return ResponseEntity.ok(response);
    }

    // TCC do aluno autenticado
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<TccResponseDTO> meuTcc(Authentication authentication) {

        return ResponseEntity.ok(
                tccService.findMeuTcc(authentication.getName())
        );
    }

    // TCCs de um aluno
    @PreAuthorize("hasAnyRole('COORDENADOR')")
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<TccResponseDTO>> findByAluno(@PathVariable Long alunoId) {
        List<TccResponseDTO> response = tccService.findByAlunoId(alunoId);
        return ResponseEntity.ok(response);
    }

    // TCCs de um professor
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<TccResponseDTO>> findByProfessor(@PathVariable Long professorId) {
        List<TccResponseDTO> response = tccService.findByProfessorId(professorId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/me")
    public ResponseEntity<List<TccResponseDTO>> meusOrientandos(Authentication authentication) {
        return ResponseEntity.ok(tccService.findByProfessorEmail(authentication.getName()));
    }

    // Buscar TCC por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TccResponseDTO> findById(@PathVariable Long id) {

        TccResponseDTO response = tccService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar TCC do aluno autenticado
    @PreAuthorize("hasRole('ALUNO')")
    @PutMapping("/me")
    public ResponseEntity<TccResponseDTO> atualizarMeuTcc(
            Authentication authentication,
            @RequestBody TccRequestDTO request) {

        return ResponseEntity.ok(
                tccService.updateMeuTcc(
                        authentication.getName(),
                        request
                )
        );
    }

    // Atualizar TCC por id
    @PreAuthorize("hasAnyRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<TccResponseDTO> updateTcc(
            @PathVariable Long id,
            @Valid @RequestBody TccRequestDTO request) {

        TccResponseDTO response = tccService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar TCC
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTcc(@PathVariable Long id) {
        tccService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
