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

    public void deleteById(Long id) {
        tccRepository.deleteById(id);
    }
}
