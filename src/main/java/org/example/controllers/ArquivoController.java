package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import org.example.dto.request.ArquivoRequestDTO;
import org.example.dto.response.ArquivoResponseDTO;
import org.example.enums.TipoArquivo;
import org.example.services.ArquivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivoService arquivoService;

    // Upload Arquivo
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ArquivoResponseDTO> uploadArquivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("submissaoId") Long submissaoId,
            @RequestParam("tipo") TipoArquivo tipo
    ) throws IOException {

        ArquivoResponseDTO response =
                arquivoService.upload(file, submissaoId, tipo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    //Dowload arquivo
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadArquivo(
            @PathVariable Long id
    ) throws Exception {

        Resource resource =
                arquivoService.downloadArquivo(id);

        ArquivoResponseDTO arquivo =
                arquivoService.findById(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                arquivo.getNomeArquivo() +
                                "\""
                )
                .body(resource);
    }

    // Listar Arquivos
    @GetMapping
    public ResponseEntity<List<ArquivoResponseDTO>> listAll() {

        List<ArquivoResponseDTO> response = arquivoService.findAll();

        return ResponseEntity.ok(response);
    }

    // Buscar arquivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ArquivoResponseDTO> findById(@PathVariable Long id) {

        ArquivoResponseDTO response = arquivoService.findById(id);

        return ResponseEntity.ok(response);
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
