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
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Feedback feedback = Feedback.builder()
                .comentario(request.getComentario())
                .nota(request.getNota())
                .data(request.getData() != null ? request.getData() : LocalDateTime.now())
                .submissao(submissao)
                .professor(professor)
                .build();

        feedback = feedbackRepository.save(feedback);

        return new FeedbackResponseDTO(
                feedback.getId(),
                feedback.getComentario(),
                feedback.getNota(),
                feedback.getData(),
                feedback.getSubmissao().getId(),
                feedback.getProfessor().getId()
        );
    }

    // Listar
    public List<FeedbackResponseDTO> findAll() {

        List<Feedback> feedbacks = feedbackRepository.findAll();

        return feedbacks.stream()
                .map(feedback -> new FeedbackResponseDTO(
                        feedback.getId(),
                        feedback.getComentario(),
                        feedback.getNota(),
                        feedback.getData(),
                        feedback.getSubmissao().getId(),
                        feedback.getProfessor().getId()
                ))
                .toList();
    }

    // Buscar por ID
    public FeedbackResponseDTO findById(Long id) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Feedback não encontrado"));

        return new FeedbackResponseDTO(
                feedback.getId(),
                feedback.getComentario(),
                feedback.getNota(),
                feedback.getData(),
                feedback.getSubmissao().getId(),
                feedback.getProfessor().getId()
        );
    }

    // Atualizar
    public FeedbackResponseDTO update(Long id, FeedbackRequestDTO request) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback não encontrado"));

        Submissao submissao = submissaoRepository.findById(request.getSubmissaoId())
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        feedback.setComentario(request.getComentario());
        feedback.setNota(request.getNota());
        feedback.setSubmissao(submissao);
        feedback.setProfessor(professor);

        // Mantém data atual se vier null
        feedback.setData(
                request.getData() != null
                        ? request.getData()
                        : feedback.getData()
        );

        feedback = feedbackRepository.save(feedback);

        return new FeedbackResponseDTO(
                feedback.getId(),
                feedback.getComentario(),
                feedback.getNota(),
                feedback.getData(),
                feedback.getSubmissao().getId(),
                feedback.getProfessor().getId()
        );
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }
}
