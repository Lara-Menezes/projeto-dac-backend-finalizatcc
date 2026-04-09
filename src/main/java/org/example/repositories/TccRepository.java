package org.example.repositories;

import org.example.enums.StatusTcc;
import org.example.model.Tcc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TccRepository extends JpaRepository<Tcc, Long> {
    List<Tcc> findByAlunoId(Long alunoId);
    List<Tcc> findByOrientadorId(Long orientadorId);
    List<Tcc> findByStatus(StatusTcc status);
    Optional<Tcc> findByAreaId(Long areaId);
}
