package org.example.repositories;

import org.example.enums.StatusSubmissao;
import org.example.model.Submissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissaoRepository extends JpaRepository<Submissao, Long> {
    List<Submissao> findByTccId(Long tccId);
    List<Submissao> findByStatus(StatusSubmissao status);
}
