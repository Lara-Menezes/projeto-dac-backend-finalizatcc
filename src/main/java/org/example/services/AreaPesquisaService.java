package org.example.services;


import lombok.RequiredArgsConstructor;
import java.util.List;
import org.example.dto.request.AreaPesquisaRequestDTO;
import org.example.dto.response.AreaPesquisaResponseDTO;
import org.example.model.AreaPesquisa;
import org.example.repositories.AreaPesquisaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaPesquisaService{
    // Métodos de CRUD para Área de Pesquisa

    private final AreaPesquisaRepository areaPesquisaRepository;

    // Salvar
    public AreaPesquisaResponseDTO save(AreaPesquisaRequestDTO request) {

        AreaPesquisa areaPesquisa = new AreaPesquisa();
        areaPesquisa.setNome(request.getNome());
        areaPesquisa = areaPesquisaRepository.save(areaPesquisa);
        
        return new AreaPesquisaResponseDTO(
                areaPesquisa.getId(),
                areaPesquisa.getNome()
        );
    }

    // Listar
    public List<AreaPesquisaResponseDTO> findAll() {

        List<AreaPesquisa> areas = areaPesquisaRepository.findAll();

        return areas.stream()
                .map(area -> new AreaPesquisaResponseDTO(
                        area.getId(),
                        area.getNome()
                ))
                .toList();
    }

    // Atualizar
    public AreaPesquisaResponseDTO update(Long id, AreaPesquisaRequestDTO request) {

        AreaPesquisa areaPesquisa = areaPesquisaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área de pesquisa não encontrada"));

        areaPesquisa.setNome(request.getNome());

        areaPesquisa = areaPesquisaRepository.save(areaPesquisa);

        return new AreaPesquisaResponseDTO(
                areaPesquisa.getId(),
                areaPesquisa.getNome()
        );
    }

    //Deletar
    public void deleteById(Long id) {
        areaPesquisaRepository.deleteById(id);
    }

}
