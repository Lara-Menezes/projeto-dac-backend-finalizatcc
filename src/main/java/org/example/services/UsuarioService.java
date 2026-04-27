package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UsuarioRequestDTO;
import org.example.dto.response.UsuarioResponseDTO;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final AvaliadorRepository avaliadorRepository;
    private final TccRepository tccRepository;
    private final BancaRepository bancaRepository;
    private final SubmissaoRepository submissaoRepository;
    private final ArquivoRepository arquivoRepository;
    private final FeedbackRepository feedbackRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO save(UsuarioRequestDTO request) {
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha())) // ✅ CRIPTOGRAFAR
                .tipo(request.getTipo())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .createdAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now())
                .build();

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.getAtivo(),
                usuario.getCreatedAt()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            List<Tcc> tccs = tccRepository.findByAlunoId(id);
            for (Tcc tcc : tccs) {
                deleteTccDependencies(tcc.getId());
                tccRepository.delete(tcc);
            }
        } else if (usuario.getTipo() == TipoUsuario.PROFESSOR) {
            feedbackRepository.deleteAll(feedbackRepository.findByProfessorId(id));

            List<Tcc> tccs = Stream.concat(
                    tccRepository.findByOrientadorId(id).stream(),
                    tccRepository.findByCoorientadorId(id).stream()
            ).distinct().collect(Collectors.toList());

            for (Tcc tcc : tccs) {
                deleteTccDependencies(tcc.getId());
                tccRepository.delete(tcc);
            }
            avaliadorRepository.deleteAll(avaliadorRepository.findByProfessorId(id));
        }

        // Remover registros de auditoria deste usuário antes de deletar
        auditoriaRepository.deleteAll(auditoriaRepository.findByUsuarioId(id));

        // Usuario tem CascadeType.ALL para Aluno/Professor, então deleta ambos
        usuarioRepository.deleteById(id);
    }

    private void deleteTccDependencies(Long tccId) {
        List<Submissao> submissoes = submissaoRepository.findByTccId(tccId);
        for (Submissao submissao : submissoes) {
            arquivoRepository.deleteAll(arquivoRepository.findBySubmissaoId(submissao.getId()));
            feedbackRepository.deleteAll(feedbackRepository.findBySubmissaoId(submissao.getId()));
            submissaoRepository.delete(submissao);
        }
        bancaRepository.findByTccId(tccId).ifPresent(banca -> {
            avaliadorRepository.deleteAll(avaliadorRepository.findByBancaId(banca.getId()));
            bancaRepository.delete(banca);
        });
    }
}
