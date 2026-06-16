package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.FeedbackRequestDTO;
import org.example.dto.response.FeedbackResponseDTO;
import org.example.services.FeedBackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedBackService feedBackService;

    // Criar Feedback
    @PostMapping("/create")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@Valid @RequestBody FeedbackRequestDTO request) {
        FeedbackResponseDTO response = feedBackService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar feedbacks
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> listAll() {

        List<FeedbackResponseDTO> response = feedBackService.findAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/submissao/{submissaoId}")
    public ResponseEntity<List<FeedbackResponseDTO>> findBySubmissao(@PathVariable Long submissaoId) {
        List<FeedbackResponseDTO> response = feedBackService.findBySubmissaoId(submissaoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tcc/{tccId}")
    public ResponseEntity<List<FeedbackResponseDTO>> findByTcc(@PathVariable Long tccId) {
        List<FeedbackResponseDTO> response = feedBackService.findByTccId(tccId);
        return ResponseEntity.ok(response);
    }

    // Buscar feedback por ID
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> findById(@PathVariable Long id) {

        FeedbackResponseDTO response = feedBackService.findById(id);

        return ResponseEntity.ok(response);
    }

    // Atualizar feedback
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequestDTO request) {

        FeedbackResponseDTO response = feedBackService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedBackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
