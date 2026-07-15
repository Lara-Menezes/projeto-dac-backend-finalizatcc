package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.BancaRequestDTO;
import org.example.dto.response.BancaResponseDTO;
import org.example.enums.StatusTcc;
import org.example.model.Avaliador;
import org.example.model.Banca;
import org.example.model.Tcc;
import org.example.repositories.AvaliadorRepository;
import org.example.repositories.BancaRepository;
import org.example.repositories.TccRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BancaService {

    private final BancaRepository bancaRepository;
    private final TccRepository tccRepository;
    private final AvaliadorRepository avaliadorRepository;

    public BancaResponseDTO save(BancaRequestDTO request) {
        Tcc tcc = tccRepository.findById(request.getTccId())
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        validarCriacaoBanca(tcc);

        Banca banca = Banca.builder()
                .data(request.getData())
                .local(request.getLocal())
                .notaFinal(request.getNotaFinal())
                .tcc(tcc)
                .build();

        updateTccStatusFromBanca(tcc, request.getNotaFinal());
        return toResponse(bancaRepository.save(banca));
    }

    // Listar
    public List<BancaResponseDTO> findAll() {

        List<Banca> bancas = bancaRepository.findAll();

        return bancas.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<BancaResponseDTO> findForAluno(String email) {
        return bancaRepository.findByTccAlunoUsuarioEmail(email).stream()
                .map(this::toResponse).toList();
    }

    public List<BancaResponseDTO> findForProfessor(String email) {
        Stream<Banca> orientadas = Stream.concat(
                bancaRepository.findByTccOrientadorUsuarioEmail(email).stream(),
                bancaRepository.findByTccCoorientadorUsuarioEmail(email).stream());
        Stream<Banca> participacoes = avaliadorRepository.findByProfessorUsuarioEmail(email).stream()
                .map(Avaliador::getBanca);
        return Stream.concat(orientadas, participacoes)
                .distinct().map(this::toResponse).toList();
    }

    public BancaResponseDTO updateGradeForProfessor(Long id, String email, Double notaFinal) {
        Banca banca = bancaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banca não encontrada"));
        boolean orientador = banca.getTcc().getOrientador().getUsuario().getEmail().equalsIgnoreCase(email);
        boolean avaliador = avaliadorRepository.findByBancaId(id).stream()
                .anyMatch(item -> item.getProfessor().getUsuario().getEmail().equalsIgnoreCase(email));
        if (!orientador && !avaliador) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Professor não participa desta banca");
        }
        banca.setNotaFinal(notaFinal);
        updateTccStatusFromBanca(banca.getTcc(), notaFinal);
        return toResponse(bancaRepository.save(banca));
    }

    public BancaResponseDTO findByTccId(Long tccId) {
        return bancaRepository.findByTccId(tccId)
                .map(this::toResponse)
                .orElse(null);
    }

    // Buscar por ID
    public BancaResponseDTO findById(Long id) {

        Banca banca = bancaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Banca não encontrada"));

        return toResponse(banca);
    }

    // Atualizar
    public BancaResponseDTO update(Long id, BancaRequestDTO request) {

        Banca banca = bancaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banca não encontrada"));

        Tcc tcc = tccRepository.findById(request.getTccId())
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        banca.setData(request.getData());
        banca.setLocal(request.getLocal());
        banca.setNotaFinal(request.getNotaFinal());
        banca.setTcc(tcc);

        updateTccStatusFromBanca(tcc, request.getNotaFinal());
        return toResponse(bancaRepository.save(banca));
    }

    public void deleteById(Long id) {
        Banca banca = bancaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Banca não encontrada"));

        Tcc tcc = banca.getTcc();

        bancaRepository.delete(banca);

        if (tcc != null && tcc.getStatus() == StatusTcc.EM_BANCA) {
            tcc.setStatus(StatusTcc.EM_DESENVOLVIMENTO);
            tccRepository.save(tcc);
        }
    }

    private BancaResponseDTO toResponse(Banca banca) {
        Tcc tcc = banca.getTcc();
        BancaResponseDTO response = new BancaResponseDTO(
                banca.getId(),
                banca.getData(),
                banca.getLocal(),
                banca.getNotaFinal(),
                tcc.getId()
        );

        response.setTccTitulo(tcc.getTitulo());
        if (tcc.getAluno() != null) {
            response.setAlunoId(tcc.getAluno().getId());
            if (tcc.getAluno().getUsuario() != null) {
                response.setAlunoNome(tcc.getAluno().getUsuario().getNome());
            }
        }
        if (tcc.getOrientador() != null) {
            response.setOrientadorId(tcc.getOrientador().getId());
            if (tcc.getOrientador().getUsuario() != null) {
                response.setOrientadorNome(tcc.getOrientador().getUsuario().getNome());
            }
        }

        List<Avaliador> avaliadores = avaliadorRepository.findByBancaId(banca.getId());
        response.setAvaliadores(avaliadores.stream()
                .map(avaliador -> new BancaResponseDTO.AvaliadorResumoDTO(
                        avaliador.getId(),
                        avaliador.getProfessor().getId(),
                        avaliador.getProfessor().getUsuario() != null
                                ? avaliador.getProfessor().getUsuario().getNome()
                                : null,
                        avaliador.getPapel() != null ? avaliador.getPapel().name() : null
                ))
                .toList());

        return response;
    }

    private void validarCriacaoBanca(Tcc tcc) {
        if (tcc.getStatus() == StatusTcc.APROVADO) {
            throw new RuntimeException("TCC aprovado não pode receber banca");
        }

        if (tcc.getStatus() == StatusTcc.REPROVADO) {
            throw new RuntimeException("TCC reprovado não pode receber banca");
        }

        if (bancaRepository.findByTccId(tcc.getId()).isPresent()) {
            throw new RuntimeException("Este TCC já possui banca cadastrada");
        }
    }

    private void updateTccStatusFromBanca(Tcc tcc, Double notaFinal) {
        if (notaFinal != null) {
            tcc.setStatus(notaFinal >= 7.0 ? StatusTcc.APROVADO : StatusTcc.REPROVADO);
        } else if (tcc.getStatus() == StatusTcc.EM_DESENVOLVIMENTO) {
            tcc.setStatus(StatusTcc.EM_BANCA);
        }
        tccRepository.save(tcc);
    }
}
