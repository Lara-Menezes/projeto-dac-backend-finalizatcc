package org.example.repositories;

import org.example.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    Optional<Arquivo> findById(Long id);
    List<Arquivo> findAll();
    List<Arquivo> findBySubmissaoId(Long submissaoId);
}
