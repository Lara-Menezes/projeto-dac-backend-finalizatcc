package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.TccRequestDTO;
import org.example.dto.response.TccResponseDTO;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TccService {

    private final TccRepository tccRepository;
    private final AreaPesquisaRepository areaPesquisaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final BancaRepository bancaRepository;
    private final AvaliadorRepository avaliadorRepository;
    private final SubmissaoRepository submissaoRepository;
    private final ArquivoRepository arquivoRepository;
    private final FeedbackRepository feedbackRepository;

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

    @Transactional
    public void deleteById(Long id) {
        Tcc tcc = tccRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        List<Submissao> submissoes = submissaoRepository.findByTccId(id);
        for (Submissao submissao : submissoes) {
            arquivoRepository.deleteAll(arquivoRepository.findBySubmissaoId(submissao.getId()));
            feedbackRepository.deleteAll(feedbackRepository.findBySubmissaoId(submissao.getId()));
            submissaoRepository.delete(submissao);
        }

        bancaRepository.findByTccId(id).ifPresent(banca -> {
            avaliadorRepository.deleteAll(avaliadorRepository.findByBancaId(banca.getId()));
            bancaRepository.delete(banca);
        });

        tccRepository.delete(tcc);
    }
}
