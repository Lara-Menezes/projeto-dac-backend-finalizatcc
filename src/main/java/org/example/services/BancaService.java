package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.BancaRequestDTO;
import org.example.dto.response.BancaResponseDTO;
import org.example.model.Banca;
import org.example.model.Tcc;
import org.example.repositories.BancaRepository;
import org.example.repositories.TccRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BancaService {

    private final BancaRepository bancaRepository;
    private final TccRepository tccRepository;

    public BancaResponseDTO save(BancaRequestDTO request) {
        Tcc tcc = tccRepository.findById(request.getTccId())
                .orElseThrow(() -> new RuntimeException("TCC não encontrado"));

        Banca banca = Banca.builder()
                .data(request.getData())
                .local(request.getLocal())
                .notaFinal(request.getNotaFinal())
                .tcc(tcc)
                .build();

        banca = bancaRepository.save(banca);

        return new BancaResponseDTO(
                banca.getId(),
                banca.getData(),
                banca.getLocal(),
                banca.getNotaFinal(),
                banca.getTcc().getId()
        );
    }

    public void deleteById(Long id) {
        bancaRepository.deleteById(id);
    }
}
