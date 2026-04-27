package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AlunoRequestDTO;
import org.example.dto.response.AlunoResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
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
    public AlunoResponseDTO save(AlunoRequestDTO request) {
        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha())) // ✅ CRIPTOGRAFAR
                .tipo(TipoUsuario.ALUNO)
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .build();
        usuario = usuarioRepository.save(usuario);

        // Criar Aluno
        Aluno aluno = new Aluno();
        aluno.setUsuario(usuario);
        aluno.setMatricula(request.getMatricula());
        aluno.setCurso(request.getCurso());
        aluno.setPeriodo(request.getPeriodo());
        aluno = alunoRepository.save(aluno);

        // Retornar DTO
        return new AlunoResponseDTO(
                aluno.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                aluno.getMatricula(),
                aluno.getCurso(),
                aluno.getPeriodo()
        );
    }

    // Excluir (Delete) com cascata
    @Transactional
    public void deleteById(Long id) {
        List<Tcc> tccs = tccRepository.findByAlunoId(id);

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

        // Remover registros de auditoria antes de deletar o usuario
        auditoriaRepository.deleteAll(auditoriaRepository.findByUsuarioId(id));

        // Usuario tem CascadeType.ALL para Aluno, então deleta ambos
        usuarioRepository.deleteById(id);
    }
}
