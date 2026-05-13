package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.SubmissaoRequestDTO;
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
}
