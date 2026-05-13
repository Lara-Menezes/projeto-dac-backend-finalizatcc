package org.example.repositories;

import org.example.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySubmissaoId(Long submissaoId);
    List<Feedback> findByProfessorId(Long professorId);
}
