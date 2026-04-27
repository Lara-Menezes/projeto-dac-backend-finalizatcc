package org.example.repositories;

import org.example.enums.StatusTcc;
import org.example.model.Tcc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TccRepository extends JpaRepository<Tcc, Long> {
    List<Tcc> findByAlunoId(Long alunoId);
    List<Tcc> findByOrientadorId(Long orientadorId);
    List<Tcc> findByCoorientadorId(Long coorientadorId);  // Adicione esta linha
    List<Tcc> findByStatus(StatusTcc status);
    List<Tcc> findByAreaId(Long areaId);
}
