package org.example.services;

import org.example.enums.StatusSubmissao;
import org.example.model.*;
import org.example.repositories.ArquivoRepository;
import org.example.repositories.SubmissaoRepository;
import org.example.repositories.TccRepository;
import org.example.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationOwnershipTest {

    @Test
    void professorCannotChangeSubmissionFromAnotherAdvisor() {
        SubmissaoRepository repository = mock(SubmissaoRepository.class);
        Submissao submissao = submissionOwnedBy("aluno@example.com", "orientador@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(submissao));
        SubmissaoService service = new SubmissaoService(repository, mock(TccRepository.class),
                mock(UsuarioRepository.class));

        assertThrows(ResponseStatusException.class, () ->
                service.updateStatusForProfessor(1L, "intruso@example.com", StatusSubmissao.ACEITO));
        verify(repository, never()).save(any());
    }

    @Test
    void advisorCanChangeOwnStudentsSubmission() {
        SubmissaoRepository repository = mock(SubmissaoRepository.class);
        Submissao submissao = submissionOwnedBy("aluno@example.com", "orientador@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(submissao));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        SubmissaoService service = new SubmissaoService(repository, mock(TccRepository.class),
                mock(UsuarioRepository.class));

        service.updateStatusForProfessor(1L, "orientador@example.com", StatusSubmissao.ACEITO);

        assertEquals(StatusSubmissao.ACEITO, submissao.getStatus());
        verify(repository).save(submissao);
    }

    @Test
    void userCannotReadFilesFromUnrelatedSubmission() {
        SubmissaoRepository submissions = mock(SubmissaoRepository.class);
        when(submissions.findById(1L)).thenReturn(Optional.of(
                submissionOwnedBy("aluno@example.com", "orientador@example.com")));
        ArquivoService service = new ArquivoService(mock(ArquivoRepository.class), submissions);

        assertThrows(ResponseStatusException.class, () ->
                service.findBySubmissaoAuthorized(1L, "intruso@example.com", false));
    }

    private Submissao submissionOwnedBy(String alunoEmail, String professorEmail) {
        Usuario alunoUsuario = Usuario.builder().email(alunoEmail).ativo(true).build();
        Aluno aluno = new Aluno();
        aluno.setUsuario(alunoUsuario);
        Usuario professorUsuario = Usuario.builder().email(professorEmail).ativo(true).build();
        Professor professor = new Professor();
        professor.setUsuario(professorUsuario);
        Tcc tcc = Tcc.builder().aluno(aluno).orientador(professor).build();
        return Submissao.builder().id(1L).versao(1).status(StatusSubmissao.ENVIADO).tcc(tcc).build();
    }
}
