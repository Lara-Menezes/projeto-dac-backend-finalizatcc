package org.example.services;

import jakarta.transaction.Transactional;
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

    // Listar
    public List<UsuarioResponseDTO> findAll() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo(),
                        usuario.getAtivo(),
                        usuario.getCreatedAt()
                ))
                .toList();
    }

    // Buscar por ID
    public UsuarioResponseDTO findById(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo(),
                usuario.getAtivo(),
                usuario.getCreatedAt()
        );
    }

    // Atualizar
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        // Atualiza senha apenas se enviada
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(request.getSenha());
        }

        usuario.setTipo(request.getTipo());

        usuario.setAtivo(
                request.getAtivo() != null
                        ? request.getAtivo()
                        : usuario.getAtivo()
        );

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

    //Deletar
    @Transactional
    public void deleteById(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        boolean possuiVinculo = false;

        // Aluno
        if (usuario.getTipo() == TipoUsuario.ALUNO) {

            possuiVinculo = tccRepository.existsByAlunoId(id);

            if (possuiVinculo) {
                usuario.setAtivo(false);
                usuarioRepository.save(usuario);

                return;
            }
            alunoRepository.deleteById(id);
        }

        // Professor
        else if (usuario.getTipo() == TipoUsuario.PROFESSOR) {

            boolean orientador = tccRepository.existsByOrientadorId(id);
            boolean coorientador = tccRepository.existsByCoorientadorId(id);
            boolean avaliador = avaliadorRepository.existsByProfessorId(id);

            possuiVinculo = orientador || coorientador || avaliador;

            if (possuiVinculo) {
                usuario.setAtivo(false);
                usuarioRepository.save(usuario);

                return;
            }
            professorRepository.deleteById(id);
        }

        // Coordenador
        // sem vínculos históricos por enquanto

        usuarioRepository.deleteById(id);
    }

    // Reativar
    @Transactional
    public void reativarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAtivo(true);

        usuarioRepository.save(usuario);
    }
}
