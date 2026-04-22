package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UsuarioRequestDTO;
import org.example.dto.response.UsuarioResponseDTO;
import org.example.enums.TipoUsuario;
import org.example.model.Avaliador;
import org.example.model.Banca;
import org.example.model.Tcc;
import org.example.model.Usuario;
import org.example.repositories.AlunoRepository;
import org.example.repositories.AvaliadorRepository;
import org.example.repositories.BancaRepository;
import org.example.repositories.ProfessorRepository;
import org.example.repositories.TccRepository;
import org.example.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

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

    public UsuarioResponseDTO save(UsuarioRequestDTO request) {
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha()) // Nota: em produção, criptografar senha
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

    public void deleteById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() == TipoUsuario.ALUNO) {
            // Lógica de cascata para Aluno
            List<Tcc> tccs = tccRepository.findByAlunoId(id);
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
            alunoRepository.deleteById(id);
        } else if (usuario.getTipo() == TipoUsuario.PROFESSOR) {
            // Lógica de cascata para Professor
            List<Tcc> tccsOrientador = tccRepository.findByOrientadorId(id);
            List<Tcc> tccsCoorientador = tccRepository.findByCoorientadorId(id);
            List<Tcc> tccs = Stream.concat(tccsOrientador.stream(), tccsCoorientador.stream())
                    .distinct()
                    .collect(Collectors.toList());
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
            List<Avaliador> avaliadoresProf = avaliadorRepository.findByProfessorId(id);
            for (Avaliador av : avaliadoresProf) {
                avaliadorRepository.delete(av);
            }
            professorRepository.deleteById(id);
        }
        // Para COORDENADOR, sem cascata adicional

        usuarioRepository.deleteById(id);
    }
}
