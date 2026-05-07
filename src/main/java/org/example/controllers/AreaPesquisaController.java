package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.example.dto.request.AreaPesquisaRequestDTO;
import org.example.dto.response.AreaPesquisaResponseDTO;
import org.example.services.AreaPesquisaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/areas-pesquisa")
@RequiredArgsConstructor
public class AreaPesquisaController {

    private final AreaPesquisaService areaPesquisaService;

    // Criar Área de Pesquisa
    @PostMapping("/create")
    public ResponseEntity<AreaPesquisaResponseDTO> createAreaPesquisa(@Valid @RequestBody AreaPesquisaRequestDTO request) {
        AreaPesquisaResponseDTO response = areaPesquisaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar Áreas de Pesquisa
    @GetMapping
    public ResponseEntity<List<AreaPesquisaResponseDTO>> listAll() {

        List<AreaPesquisaResponseDTO> response = areaPesquisaService.findAll();

        return ResponseEntity.ok(response);
    }

    // Atualizar Área de Pesquisa
    @PutMapping("/{id}")
    public ResponseEntity<AreaPesquisaResponseDTO> updateAreaPesquisa(
            @PathVariable Long id,
            @Valid @RequestBody AreaPesquisaRequestDTO request) {

        AreaPesquisaResponseDTO response = areaPesquisaService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // Deletar Área de Pesquisa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAreaPesquisa(@PathVariable Long id) {
        areaPesquisaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
