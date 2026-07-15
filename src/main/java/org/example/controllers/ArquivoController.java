package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.example.dto.request.ArquivoRequestDTO;
import org.example.dto.response.ArquivoResponseDTO;
import org.example.enums.TipoArquivo;
import org.example.services.ArquivoService;
import org.example.services.ArquivoService.ArquivoDownload;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;

    // Criar Arquivo manualmente
    @PostMapping("/create")
    public ResponseEntity<ArquivoResponseDTO> createArquivo(@Valid @RequestBody ArquivoRequestDTO request) {
        ArquivoResponseDTO response = arquivoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Upload Arquivo
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoResponseDTO> uploadArquivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("submissaoId") Long submissaoId,
            @RequestParam(value = "tipo", defaultValue = "MANUSCRITO") TipoArquivo tipo) {
        ArquivoResponseDTO response = arquivoService.upload(file, submissaoId, tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Arquivos
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping
    public ResponseEntity<List<ArquivoResponseDTO>> listAll() {

        List<ArquivoResponseDTO> response = arquivoService.findAll();

        return ResponseEntity.ok(response);
    }

    // Buscar para aluno autenticado
    @PreAuthorize("hasRole('ALUNO')")
    @GetMapping("/me")
    public ResponseEntity<List<ArquivoResponseDTO>> meusArquivos(
            Authentication authentication) {

        return ResponseEntity.ok(
                arquivoService.findByAlunoEmail(authentication.getName())
        );
    }

    // Busca arquivo por Submissão ID
    @GetMapping("/submissao/{submissaoId}")
    public ResponseEntity<List<ArquivoResponseDTO>> findBySubmissao(@PathVariable Long submissaoId) {
        List<ArquivoResponseDTO> response = arquivoService.findBySubmissaoId(submissaoId);
        return ResponseEntity.ok(response);
    }

    // Buscar arquivo por ID
    @PreAuthorize("hasRole('COORDENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ArquivoResponseDTO> findById(@PathVariable Long id) {

        ArquivoResponseDTO response = arquivoService.findById(id);

        return ResponseEntity.ok(response);
    }

    //Dowload
    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable Long id) {
        ArquivoDownload download = arquivoService.download(id);
        String mimeType = download.mimeType() != null ? download.mimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(download.nomeArquivo(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .body(download.resource());
    }

    // Atualizar Arquivo
    @PreAuthorize("hasRole('COORDENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ArquivoResponseDTO> updateArquivo(
            @PathVariable Long id,
            @Valid @RequestBody ArquivoRequestDTO request) {

        ArquivoResponseDTO response = arquivoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Arquivo
    @PreAuthorize("hasRole('COORDENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable Long id) {
        arquivoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Visualizar sem baixar
    @PreAuthorize("hasRole('COORDENADOR', 'PROFESSOR')")
    @GetMapping("/{id}/visualizar")
    public ResponseEntity<?> visualizar(@PathVariable Long id) {

        ArquivoDownload download = arquivoService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(download.resource());
    }
}
