package org.example.services;


import lombok.RequiredArgsConstructor;
import org.example.dto.request.AreaPesquisaRequestDTO;
import org.example.dto.response.AreaPesquisaResponseDTO;
import org.example.model.AreaPesquisa;
import org.example.repositories.AreaPesquisaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaPesquisaService{

    private final AreaPesquisaRepository areaPesquisaRepository;

    public AreaPesquisaResponseDTO save(AreaPesquisaRequestDTO request) {

        AreaPesquisa areaPesquisa = new AreaPesquisa();
        areaPesquisa.setNome(request.getNome());
        areaPesquisa = areaPesquisaRepository.save(areaPesquisa);
        
        return new AreaPesquisaResponseDTO(
                areaPesquisa.getId(),
                areaPesquisa.getNome()
        );
    }
    
    public void deleteById(Long id) {
        areaPesquisaRepository.deleteById(id);
    }
    // Métodos de CRUD para Área de Pesquisa {
}
