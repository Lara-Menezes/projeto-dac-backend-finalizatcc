package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.TccRequestDTO;
import org.example.dto.response.TccResponseDTO;
import org.example.model.*;
import org.example.enums.StatusTcc;
import org.example.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TccService {

    private final TccRepository tccRepository;
    private final AreaPesquisaRepository areaPesquisaRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;

    // Cria TCC
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

    public TccResponseDTO saveForAluno(String email, TccRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Aluno aluno = alunoRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (!tccRepository.findByAlunoId(aluno.getId()).isEmpty()) {
            throw new RuntimeException("Aluno já possui TCC cadastrado");
        }

        AreaPesquisa area = findAreaOrNull(request.getAreaId());
        Professor orientador = professorRepository.findById(request.getOrientadorId())
                .orElseThrow(() -> new RuntimeException("Orientador não encontrado"));
        Professor coorientador = findProfessorOrNull(request.getCoorientadorId(), "Coorientador não encontrado");

        Tcc tcc = Tcc.builder()
                .titulo(request.getTitulo())
                .resumo(request.getResumo())
                .area(area)
                .aluno(aluno)
                .orientador(orientador)
                .coorientador(coorientador)
                .status(StatusTcc.EM_DESENVOLVIMENTO)
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .build();
        return toResponse(tccRepository.save(tcc));
    }

    // Busca todos os TCCs
    public List<TccResponseDTO> findAll() {
        return tccRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca por email
    public TccResponseDTO findMeuTcc(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tcc tcc = tccRepository.findByAlunoId(usuario.getId())
                .stream()
                .findFirst()
                .orElse(null);

        return tcc == null ? null : toResponse(tcc);
    }

    // Busca por Aluno ID
    public List<TccResponseDTO> findByAlunoId(Long alunoId) {
        return tccRepository.findByAlunoId(alunoId).stream()
                .map(this::toResponse)
                .toList();
    }

    // Busca por Professor ID
    public List<TccResponseDTO> findByProfessorId(Long professorId) {
        return tccRepository.findByOrientadorId(professorId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TccResponseDTO> findByProfessorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return java.util.stream.Stream.concat(
                        tccRepository.findByOrientadorId(usuario.getId()).stream(),
                        tccRepository.findByCoorientadorId(usuario.getId()).stream())
                .distinct()
                .map(this::toResponse)
                .toList();
    }

    // Busca por TCC ID
    public TccResponseDTO findById(Long id) {
        Tcc tcc = tccRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TCC nao encontrado"));

        return toResponse(tcc);
    }

    // Atualiza pelo email
    public TccResponseDTO updateMeuTcc(
            String email,
            TccRequestDTO request) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tcc tcc = tccRepository.findByAlunoId(usuario.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        AreaPesquisa area = findAreaOrNull(request.getAreaId());

        Professor orientador = professorRepository.findById(request.getOrientadorId())
                .orElseThrow(() -> new RuntimeException("Orientador não encontrado"));

        Professor coorientador = findProfessorOrNull(
                request.getCoorientadorId(),
                "Coorientador não encontrado"
        );

        tcc.setTitulo(request.getTitulo());
        tcc.setResumo(request.getResumo());
        tcc.setArea(area);
        tcc.setOrientador(orientador);
        tcc.setCoorientador(coorientador);
        // O aluno pode editar o projeto, mas não promover o próprio status.
        tcc.setDataInicio(request.getDataInicio());
        tcc.setDataFim(request.getDataFim());

        return toResponse(tccRepository.save(tcc));
    }

    // Atualiza por ID
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

    // Deleta por TCC ID
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
