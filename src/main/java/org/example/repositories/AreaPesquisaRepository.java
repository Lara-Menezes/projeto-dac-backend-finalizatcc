package org.example.repositories;

import org.example.model.AreaPesquisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaPesquisaRepository extends JpaRepository<AreaPesquisa, Long> {
    Optional<AreaPesquisa> findById(Long Long);
    List<AreaPesquisa> findByNome(String nome);
}
