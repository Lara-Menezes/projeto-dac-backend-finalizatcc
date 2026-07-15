package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.FeedbackRequestDTO;
import org.example.dto.response.FeedbackResponseDTO;
import org.example.services.FeedBackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedBackService feedBackService;

    // Criar Feedback
    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("/create")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(Authentication authentication,
                                                               @Valid @RequestBody FeedbackRequestDTO request) {
        FeedbackResponseDTO response = feedBackService.saveForProfessor(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar feedbacks
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> listAll() {

        List<FeedbackResponseDTO> response = feedBackService.findAll();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<List<FeedbackResponseDTO>> meusFeedbacks(Authentication authentication) {
        return ResponseEntity.ok(feedBackService.findForAluno(authentication.getName()));
    }

    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/me")
    public ResponseEntity<List<FeedbackResponseDTO>> meusFeedbacksEnviados(Authentication authentication) {
        return ResponseEntity.ok(feedBackService.findForProfessor(authentication.getName()));
    }

    @GetMapping("/submissao/{submissaoId}")
    @PreAuthorize("hasRole('COORDENADOR')")
    public ResponseEntity<List<FeedbackResponseDTO>> findBySubmissao(@PathVariable Long submissaoId) {
        List<FeedbackResponseDTO> response = feedBackService.findBySubmissaoId(submissaoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tcc/{tccId}")
    @PreAuthorize("hasRole('COORDENADOR')")
    public ResponseEntity<List<FeedbackResponseDTO>> findByTcc(@PathVariable Long tccId) {
        List<FeedbackResponseDTO> response = feedBackService.findByTccId(tccId);
        return ResponseEntity.ok(response);
    }

    // Buscar feedback por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> findById(@PathVariable Long id) {

        FeedbackResponseDTO response = feedBackService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar feedback
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO request) {

        FeedbackResponseDTO response = feedBackService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Feedback
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedBackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
