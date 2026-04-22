package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AlunoRequestDTO;
import org.example.dto.response.AlunoResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.*;
import org.example.repositories.*;
import org.springframework.stereotype.Service;

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

    // Adicionar (Create)
    public AlunoResponseDTO save(AlunoRequestDTO request) {
        // Criar Usuario primeiro
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha()) // Nota: em produção, criptografar senha
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
    public void deleteById(Long id) {
        // Buscar Tccs do aluno
        List<Tcc> tccs = tccRepository.findByAlunoId(id);

        // Para cada Tcc, deletar dependentes: Avaliadores da Banca, depois Banca, depois Tcc
        for (Tcc tcc : tccs) {
            Banca banca = bancaRepository.findByTccId(tcc.getId()).orElse(null);
            if (banca != null) {
                List<Avaliador> avaliadoresBanca = avaliadorRepository.findByBancaId(banca.getId());
                for (Avaliador av : avaliadoresBanca) {
                    avaliadorRepository.delete(av);
                }
                bancaRepository.delete(banca);
            }
            tccRepository.delete(tcc);
        }

        // Deletar o Aluno
        alunoRepository.deleteById(id);

        // **IMPORTANTE: Deletar o Usuario associado**
        usuarioRepository.deleteById(id);
    }
}
