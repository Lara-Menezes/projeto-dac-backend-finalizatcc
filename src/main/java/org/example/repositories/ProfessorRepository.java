package org.example.repositories;

import org.example.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.example.enums.TipoUsuario;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByUsuarioId(Long usuarioId);
    Optional<Professor> findByUsuarioEmail(String email);
    List<Professor> findByAreaAtuacao(String areaAtuacao);
    List<Professor> findByTitulacao(String titulacao);
    List<Professor> findByUsuarioTipo(TipoUsuario tipo);
}
