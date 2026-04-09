package org.example.repositories;

import org.example.model.Banca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BancaRepository extends JpaRepository<Banca, Long> {
    Optional<Banca> findByTccId(Long tccId);
}
