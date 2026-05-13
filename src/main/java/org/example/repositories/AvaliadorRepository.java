package org.example.repositories;

import org.example.model.Avaliador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliadorRepository extends JpaRepository<Avaliador, Long> {
    List<Avaliador> findByBancaId(Long bancaId);
    List<Avaliador> findByProfessorId(Long professorId);
}
