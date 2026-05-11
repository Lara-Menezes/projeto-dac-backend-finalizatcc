package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.TccRequestDTO;
import org.example.dto.response.TccResponseDTO;
import org.example.model.Aluno;
import org.example.model.AreaPesquisa;
import org.example.model.Professor;
import org.example.model.Tcc;
import org.example.repositories.AlunoRepository;
import org.example.repositories.AreaPesquisaRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.TccRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TccService {

    private final TccRepository tccRepository;
    private final AreaPesquisaRepository areaPesquisaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;

    public TccResponseDTO save(TccRequestDTO request) {
        AreaPesquisa area = null;
        if (request.getAreaId() != null) {
            area = areaPesquisaRepository.findById(request.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Área de pesquisa não encontrada"));
        }
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Professor orientador = professorRepository.findById(request.getOrientadorId())
                .orElseThrow(() -> new RuntimeException("Orientador não encontrado"));
        Professor coorientador = null;
        if (request.getCoorientadorId() != null) {
            coorientador = professorRepository.findById(request.getCoorientadorId())
                    .orElseThrow(() -> new RuntimeException("Coorientador não encontrado"));
        }

        Tcc tcc = Tcc.builder()
                .titulo(request.getTitulo())
                .resumo(request.getResumo())
                .area(area)
                .aluno(aluno)
                .orientador(orientador)
                .coorientador(coorientador)
                .status(request.getStatus())
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .build();

        tcc = tccRepository.save(tcc);

        return new TccResponseDTO(
                tcc.getId(),
                tcc.getTitulo(),
                tcc.getResumo(),
                tcc.getArea() != null ? tcc.getArea().getId() : null,
                tcc.getAluno().getId(),
                tcc.getOrientador().getId(),
                tcc.getCoorientador() != null ? tcc.getCoorientador().getId() : null,
                tcc.getStatus(),
                tcc.getDataInicio(),
                tcc.getDataFim()
        );
    }

    // Listar
    public List<TccResponseDTO> findAll() {

        List<Tcc> tccs = tccRepository.findAll();

        return tccs.stream()
                .map(tcc -> new TccResponseDTO(
                        tcc.getId(),
                        tcc.getTitulo(),
                        tcc.getResumo(),
                        tcc.getArea() != null
                                ? tcc.getArea().getId()
                                : null,
                        tcc.getAluno().getId(),
                        tcc.getOrientador().getId(),
                        tcc.getCoorientador() != null
                                ? tcc.getCoorientador().getId()
                                : null,
                        tcc.getStatus(),
                        tcc.getDataInicio(),
                        tcc.getDataFim()
                ))
                .toList();
    }

    // Buscar por ID
    public TccResponseDTO findById(Long id) {

        Tcc tcc = tccRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("TCC não encontrado"));

        return new TccResponseDTO(
                tcc.getId(),
                tcc.getTitulo(),
                tcc.getResumo(),
                tcc.getArea() != null
                        ? tcc.getArea().getId()
                        : null,
                tcc.getAluno().getId(),
                tcc.getOrientador().getId(),
                tcc.getCoorientador() != null
                        ? tcc.getCoorientador().getId()
                        : null,
                tcc.getStatus(),
                tcc.getDataInicio(),
                tcc.getDataFim()
        );
    }

    // Atualizar
    public TccResponseDTO update(Long id, TccRequestDTO request) {

        Tcc tcc = tccRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("TCC não encontrado"));

        AreaPesquisa area = null;

        if (request.getAreaId() != null) {
            area = areaPesquisaRepository.findById(request.getAreaId())
                    .orElseThrow(() ->
                            new RuntimeException("Área de pesquisa não encontrada"));
        }

        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado"));

        Professor orientador = professorRepository.findById(request.getOrientadorId())
                .orElseThrow(() ->
                        new RuntimeException("Orientador não encontrado"));

        Professor coorientador = null;

        if (request.getCoorientadorId() != null) {
            coorientador = professorRepository.findById(request.getCoorientadorId())
                    .orElseThrow(() ->
                            new RuntimeException("Coorientador não encontrado"));
        }

        tcc.setTitulo(request.getTitulo());
        tcc.setResumo(request.getResumo());
        tcc.setArea(area);
        tcc.setAluno(aluno);
        tcc.setOrientador(orientador);
        tcc.setCoorientador(coorientador);
        tcc.setStatus(request.getStatus());
        tcc.setDataInicio(request.getDataInicio());
        tcc.setDataFim(request.getDataFim());

        tcc = tccRepository.save(tcc);

        return new TccResponseDTO(
                tcc.getId(),
                tcc.getTitulo(),
                tcc.getResumo(),
                tcc.getArea() != null ? tcc.getArea().getId() : null,
                tcc.getAluno().getId(),
                tcc.getOrientador().getId(),
                tcc.getCoorientador() != null
                        ? tcc.getCoorientador().getId()
                        : null,
                tcc.getStatus(),
                tcc.getDataInicio(),
                tcc.getDataFim()
        );
    }

    public void deleteById(Long id) {
        tccRepository.deleteById(id);
    }
}
