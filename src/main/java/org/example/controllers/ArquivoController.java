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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;

    // Criar Arquivo
    @PostMapping("/create")
    public ResponseEntity<ArquivoResponseDTO> createArquivo(@Valid @RequestBody ArquivoRequestDTO request) {
        ArquivoResponseDTO response = arquivoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoResponseDTO> uploadArquivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("submissaoId") Long submissaoId,
            @RequestParam(value = "tipo", defaultValue = "MANUSCRITO") TipoArquivo tipo) {
        ArquivoResponseDTO response = arquivoService.upload(file, submissaoId, tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Arquivos
    @GetMapping
    public ResponseEntity<List<ArquivoResponseDTO>> listAll() {

        List<ArquivoResponseDTO> response = arquivoService.findAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/submissao/{submissaoId}")
    public ResponseEntity<List<ArquivoResponseDTO>> findBySubmissao(@PathVariable Long submissaoId) {
        List<ArquivoResponseDTO> response = arquivoService.findBySubmissaoId(submissaoId);
        return ResponseEntity.ok(response);
    }

    // Buscar arquivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ArquivoResponseDTO> findById(@PathVariable Long id) {

        ArquivoResponseDTO response = arquivoService.findById(id);

        return ResponseEntity.ok(response);
    }

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
    @PutMapping("/{id}")
    public ResponseEntity<ArquivoResponseDTO> updateArquivo(
            @PathVariable Long id,
            @Valid @RequestBody ArquivoRequestDTO request) {

        ArquivoResponseDTO response = arquivoService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Arquivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArquivo(@PathVariable Long id) {
        arquivoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
