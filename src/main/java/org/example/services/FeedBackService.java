package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.FeedbackRequestDTO;
import org.example.dto.response.FeedbackResponseDTO;
import org.example.model.Feedback;
import org.example.model.Professor;
import org.example.model.Submissao;
import org.example.repositories.FeedbackRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.SubmissaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedbackRepository feedbackRepository;
    private final SubmissaoRepository submissaoRepository;
    private final ProfessorRepository professorRepository;

    public FeedbackResponseDTO save(FeedbackRequestDTO request) {
        Submissao submissao = submissaoRepository.findById(request.getSubmissaoId())
                .orElseThrow(() -> new RuntimeException("Submissao nao encontrada"));
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor nao encontrado"));

        Feedback feedback = Feedback.builder()
                .comentario(request.getComentario())
                .nota(request.getNota())
                .data(request.getData() != null ? request.getData() : LocalDateTime.now())
                .submissao(submissao)
                .professor(professor)
                .build();

        return toResponse(feedbackRepository.save(feedback));
    }

    public List<FeedbackResponseDTO> findAll() {
        return feedbackRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FeedbackResponseDTO> findBySubmissaoId(Long submissaoId) {
        return feedbackRepository.findBySubmissaoId(submissaoId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FeedbackResponseDTO> findByTccId(Long tccId) {
        return feedbackRepository.findAll().stream()
                .filter(feedback -> feedback.getSubmissao().getTcc().getId().equals(tccId))
                .map(this::toResponse)
                .toList();
    }

    public FeedbackResponseDTO findById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback nao encontrado"));

        return toResponse(feedback);
    }

    public FeedbackResponseDTO update(Long id, FeedbackRequestDTO request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback nao encontrado"));

        Submissao submissao = submissaoRepository.findById(request.getSubmissaoId())
                .orElseThrow(() -> new RuntimeException("Submissao nao encontrada"));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor nao encontrado"));

        feedback.setComentario(request.getComentario());
        feedback.setNota(request.getNota());
        feedback.setSubmissao(submissao);
        feedback.setProfessor(professor);
        feedback.setData(request.getData() != null ? request.getData() : feedback.getData());

        return toResponse(feedbackRepository.save(feedback));
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }

    private FeedbackResponseDTO toResponse(Feedback feedback) {
        FeedbackResponseDTO response = new FeedbackResponseDTO(
                feedback.getId(),
                feedback.getComentario(),
                feedback.getNota(),
                feedback.getData(),
                feedback.getSubmissao().getId(),
                feedback.getProfessor().getId()
        );
        response.setProfessorNome(
                feedback.getProfessor() != null && feedback.getProfessor().getUsuario() != null
                        ? feedback.getProfessor().getUsuario().getNome()
                        : null
        );
        return response;
    }
}
