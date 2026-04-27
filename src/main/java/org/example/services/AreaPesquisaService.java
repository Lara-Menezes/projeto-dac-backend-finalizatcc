package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AreaPesquisaRequestDTO;
import org.example.dto.response.AreaPesquisaResponseDTO;
import org.example.model.AreaPesquisa;
import org.example.model.Tcc;
import org.example.repositories.AreaPesquisaRepository;
import org.example.repositories.TccRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaPesquisaService{

    private final AreaPesquisaRepository areaPesquisaRepository;
    private final TccRepository tccRepository;

    public AreaPesquisaResponseDTO save(AreaPesquisaRequestDTO request) {

        AreaPesquisa areaPesquisa = new AreaPesquisa();
        areaPesquisa.setNome(request.getNome());
        areaPesquisa = areaPesquisaRepository.save(areaPesquisa);
        
        return new AreaPesquisaResponseDTO(
                areaPesquisa.getId(),
                areaPesquisa.getNome()
        );
    }
    
    @Transactional
    public void deleteById(Long id) {
        // Desvincular TCCs que referenciam esta área antes de deletar
        List<Tcc> tccs = tccRepository.findByAreaId(id);
        for (Tcc tcc : tccs) {
            tcc.setArea(null);
            tccRepository.save(tcc);
        }
        areaPesquisaRepository.deleteById(id);
    }
}
