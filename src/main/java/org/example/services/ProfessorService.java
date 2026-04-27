package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfessorRequestDTO;
import org.example.dto.response.ProfessorResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvaliadorRepository avaliadorRepository;
    private final TccRepository tccRepository;
    private final BancaRepository bancaRepository;
    private final SubmissaoRepository submissaoRepository;
    private final ArquivoRepository arquivoRepository;
    private final FeedbackRepository feedbackRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final PasswordEncoder passwordEncoder;

    // Adicionar (Create)
    @Transactional
    public ProfessorResponseDTO save(ProfessorRequestDTO request) {
        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha())) // ✅ CRIPTOGRAFAR
                .tipo(TipoUsuario.PROFESSOR)
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .build();
        usuario = usuarioRepository.save(usuario);

        // Criar Professor
        Professor professor = new Professor();
        professor.setUsuario(usuario);
        professor.setAreaAtuacao(request.getAreaAtuacao());
        professor.setTitulacao(request.getTitulacao());
        professor = professorRepository.save(professor);

        // Retornar DTO
        return new ProfessorResponseDTO(
                professor.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                professor.getAreaAtuacao(),
                professor.getTitulacao()
        );
    }

    // Excluir (Delete) com cascata
    @Transactional
    public void deleteById(Long id) {
        // Deletar feedbacks dados por este professor (em TCCs de outros)
        feedbackRepository.deleteAll(feedbackRepository.findByProfessorId(id));

        // Buscar TCCs onde o professor é orientador ou coorientador
        List<Tcc> tccsOrientador = tccRepository.findByOrientadorId(id);
        List<Tcc> tccsCoorientador = tccRepository.findByCoorientadorId(id);
        List<Tcc> tccs = Stream.concat(tccsOrientador.stream(), tccsCoorientador.stream())
                .distinct()
                .collect(Collectors.toList());

        for (Tcc tcc : tccs) {
            List<Submissao> submissoes = submissaoRepository.findByTccId(tcc.getId());
            for (Submissao submissao : submissoes) {
                arquivoRepository.deleteAll(arquivoRepository.findBySubmissaoId(submissao.getId()));
                feedbackRepository.deleteAll(feedbackRepository.findBySubmissaoId(submissao.getId()));
                submissaoRepository.delete(submissao);
            }

            bancaRepository.findByTccId(tcc.getId()).ifPresent(banca -> {
                avaliadorRepository.deleteAll(avaliadorRepository.findByBancaId(banca.getId()));
                bancaRepository.delete(banca);
            });

            tccRepository.delete(tcc);
        }

        // Deletar avaliações em bancas de outros TCCs onde este professor era avaliador
        avaliadorRepository.deleteAll(avaliadorRepository.findByProfessorId(id));

        // Remover registros de auditoria antes de deletar o usuario
        auditoriaRepository.deleteAll(auditoriaRepository.findByUsuarioId(id));

        // Usuario tem CascadeType.ALL para Professor, então deleta ambos
        usuarioRepository.deleteById(id);
    }
}
