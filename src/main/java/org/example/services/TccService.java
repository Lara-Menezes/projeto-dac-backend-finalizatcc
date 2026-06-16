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
        AreaPesquisa area = findAreaOrNull(request.getAreaId());
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno nao encontrado"));
        Professor orientador = professorRepository.findById(request.getOrientadorId())
                .orElseThrow(() -> new RuntimeException("Orientador nao encontrado"));
        Professor coorientador = findProfessorOrNull(request.getCoorientadorId(), "Coorientador nao encontrado");

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

        return toResponse(tccRepository.save(tcc));
    }

    public List<TccResponseDTO> findAll() {
        return tccRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TccResponseDTO> findByAlunoId(Long alunoId) {
        return tccRepository.findByAlunoId(alunoId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TccResponseDTO> findByProfessorId(Long professorId) {
        return tccRepository.findByOrientadorId(professorId).stream()
                .map(this::toResponse)
                .toList();
    }

    public TccResponseDTO findById(Long id) {
        Tcc tcc = tccRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TCC nao encontrado"));

        return toResponse(tcc);
    }

    public TccResponseDTO update(Long id, TccRequestDTO request) {
        Tcc tcc = tccRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TCC nao encontrado"));

        AreaPesquisa area = findAreaOrNull(request.getAreaId());
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno nao encontrado"));
        Professor orientador = professorRepository.findById(request.getOrientadorId())
                .orElseThrow(() -> new RuntimeException("Orientador nao encontrado"));
        Professor coorientador = findProfessorOrNull(request.getCoorientadorId(), "Coorientador nao encontrado");

        tcc.setTitulo(request.getTitulo());
        tcc.setResumo(request.getResumo());
        tcc.setArea(area);
        tcc.setAluno(aluno);
        tcc.setOrientador(orientador);
        tcc.setCoorientador(coorientador);
        tcc.setStatus(request.getStatus());
        tcc.setDataInicio(request.getDataInicio());
        tcc.setDataFim(request.getDataFim());

        return toResponse(tccRepository.save(tcc));
    }

    public void deleteById(Long id) {
        tccRepository.deleteById(id);
    }

    private AreaPesquisa findAreaOrNull(Long areaId) {
        if (areaId == null) return null;
        return areaPesquisaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Area de pesquisa nao encontrada"));
    }

    private Professor findProfessorOrNull(Long professorId, String message) {
        if (professorId == null) return null;
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException(message));
    }

    private TccResponseDTO toResponse(Tcc tcc) {
        TccResponseDTO response = new TccResponseDTO(
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

        response.setAreaNome(tcc.getArea() != null ? tcc.getArea().getNome() : null);
        response.setAlunoNome(
                tcc.getAluno() != null && tcc.getAluno().getUsuario() != null
                        ? tcc.getAluno().getUsuario().getNome()
                        : null
        );
        response.setAlunoEmail(
                tcc.getAluno() != null && tcc.getAluno().getUsuario() != null
                        ? tcc.getAluno().getUsuario().getEmail()
                        : null
        );
        response.setOrientadorNome(
                tcc.getOrientador() != null && tcc.getOrientador().getUsuario() != null
                        ? tcc.getOrientador().getUsuario().getNome()
                        : null
        );
        response.setOrientadorEmail(
                tcc.getOrientador() != null && tcc.getOrientador().getUsuario() != null
                        ? tcc.getOrientador().getUsuario().getEmail()
                        : null
        );
        response.setCoorientadorNome(
                tcc.getCoorientador() != null && tcc.getCoorientador().getUsuario() != null
                        ? tcc.getCoorientador().getUsuario().getNome()
                        : null
        );
        response.setCoorientadorEmail(
                tcc.getCoorientador() != null && tcc.getCoorientador().getUsuario() != null
                        ? tcc.getCoorientador().getUsuario().getEmail()
                        : null
        );

        return response;
    }
}
