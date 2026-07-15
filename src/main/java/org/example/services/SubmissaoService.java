package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.SubmissaoRequestDTO;
import org.example.enums.StatusSubmissao;
import org.example.model.Usuario;
import org.example.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.example.dto.response.SubmissaoResponseDTO;
import org.example.model.Submissao;
import org.example.model.Tcc;
import org.example.repositories.SubmissaoRepository;
import org.example.repositories.TccRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissaoService {

    private final SubmissaoRepository submissaoRepository;
    private final TccRepository tccRepository;
    private final UsuarioRepository usuarioRepository;

    public SubmissaoResponseDTO save(SubmissaoRequestDTO request) {
        Tcc tcc = tccRepository.findById(request.getTccId())
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        Submissao submissao = Submissao.builder()
                .versao(request.getVersao())
                .dataEnvio(request.getDataEnvio() != null ? request.getDataEnvio() : LocalDateTime.now())
                .status(request.getStatus())
                .prazoEntrega(request.getPrazoEntrega())
                .tcc(tcc)
                .build();

        submissao = submissaoRepository.save(submissao);

        return new SubmissaoResponseDTO(
                submissao.getId(),
                submissao.getVersao(),
                submissao.getDataEnvio(),
                submissao.getStatus(),
                submissao.getPrazoEntrega(),
                submissao.getTcc().getId()
        );
    }

    public SubmissaoResponseDTO saveForAluno(String email, SubmissaoRequestDTO request) {
        Tcc tcc = tccRepository.findById(request.getTccId())
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));
        if (!tcc.getAluno().getUsuario().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "TCC não pertence ao aluno autenticado");
        }
        request.setStatus(StatusSubmissao.ENVIADO);
        return save(request);
    }

    public SubmissaoResponseDTO updateStatusForProfessor(Long id, String email, StatusSubmissao status) {
        Submissao submissao = submissaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));
        Tcc tcc = submissao.getTcc();
        boolean orienta = tcc.getOrientador().getUsuario().getEmail().equalsIgnoreCase(email)
                || (tcc.getCoorientador() != null
                && tcc.getCoorientador().getUsuario().getEmail().equalsIgnoreCase(email));
        if (!orienta) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Submissão não pertence a um orientando");
        }
        submissao.setStatus(status);
        return toResponse(submissaoRepository.save(submissao));
    }

    // Listar
    public List<SubmissaoResponseDTO> findAll() {

        List<Submissao> submissoes = submissaoRepository.findAll();

        return submissoes.stream()
                .map(submissao -> new SubmissaoResponseDTO(
                        submissao.getId(),
                        submissao.getVersao(),
                        submissao.getDataEnvio(),
                        submissao.getStatus(),
                        submissao.getPrazoEntrega(),
                        submissao.getTcc().getId()
                ))
                .toList();
    }

    // Busca por Aluno email
    public List<SubmissaoResponseDTO> findByAlunoEmail(String email) {

        return submissaoRepository.findByTccAlunoUsuarioEmail(email)
                .stream()
                .map(submissao -> new SubmissaoResponseDTO(
                        submissao.getId(),
                        submissao.getVersao(),
                        submissao.getDataEnvio(),
                        submissao.getStatus(),
                        submissao.getPrazoEntrega(),
                        submissao.getTcc().getId()
                ))
                .toList();
    }

    // Busca por Professor email
    public List<SubmissaoResponseDTO> findByProfessorEmail(String email) {

        return java.util.stream.Stream.concat(
                        submissaoRepository.findByTccOrientadorUsuarioEmail(email).stream(),
                        submissaoRepository.findByTccCoorientadorUsuarioEmail(email).stream())
                .distinct()
                .map(submissao -> new SubmissaoResponseDTO(
                        submissao.getId(),
                        submissao.getVersao(),
                        submissao.getDataEnvio(),
                        submissao.getStatus(),
                        submissao.getPrazoEntrega(),
                        submissao.getTcc().getId()
                ))
                .toList();
    }

    public List<SubmissaoResponseDTO> findByTccId(Long tccId) {
        return submissaoRepository.findByTccId(tccId).stream()
                .map(submissao -> new SubmissaoResponseDTO(
                        submissao.getId(),
                        submissao.getVersao(),
                        submissao.getDataEnvio(),
                        submissao.getStatus(),
                        submissao.getPrazoEntrega(),
                        submissao.getTcc().getId()
                ))
                .toList();
    }

    // Buscar por ID
    public SubmissaoResponseDTO findById(Long id) {

        Submissao submissao = submissaoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Submissão não encontrada"));

        return new SubmissaoResponseDTO(
                submissao.getId(),
                submissao.getVersao(),
                submissao.getDataEnvio(),
                submissao.getStatus(),
                submissao.getPrazoEntrega(),
                submissao.getTcc().getId()
        );
    }

    // Atualizar
    public SubmissaoResponseDTO update(Long id, SubmissaoRequestDTO request) {

        Submissao submissao = submissaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submissão não encontrada"));

        Tcc tcc = tccRepository.findById(request.getTccId())
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        submissao.setVersao(request.getVersao());
        submissao.setStatus(request.getStatus());
        submissao.setPrazoEntrega(request.getPrazoEntrega());
        submissao.setTcc(tcc);

        // Mantém data atual se vier null
        submissao.setDataEnvio(
                request.getDataEnvio() != null
                        ? request.getDataEnvio()
                        : submissao.getDataEnvio()
        );

        submissao = submissaoRepository.save(submissao);

        return new SubmissaoResponseDTO(
                submissao.getId(),
                submissao.getVersao(),
                submissao.getDataEnvio(),
                submissao.getStatus(),
                submissao.getPrazoEntrega(),
                submissao.getTcc().getId()
        );
    }

    public void deleteById(Long id) {
        submissaoRepository.deleteById(id);
    }

    private SubmissaoResponseDTO toResponse(Submissao submissao) {
        return new SubmissaoResponseDTO(
                submissao.getId(), submissao.getVersao(), submissao.getDataEnvio(),
                submissao.getStatus(), submissao.getPrazoEntrega(), submissao.getTcc().getId());
    }
}
