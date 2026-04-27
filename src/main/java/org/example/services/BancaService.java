package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.BancaRequestDTO;
import org.example.dto.response.BancaResponseDTO;
import org.example.model.Banca;
import org.example.model.Tcc;
import org.example.repositories.AvaliadorRepository;
import org.example.repositories.BancaRepository;
import org.example.repositories.TccRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BancaService {

    private final BancaRepository bancaRepository;
    private final TccRepository tccRepository;
    private final AvaliadorRepository avaliadorRepository;

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

    @Transactional
    public void deleteById(Long id) {
        Banca banca = bancaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banca não encontrada"));
        avaliadorRepository.deleteAll(avaliadorRepository.findByBancaId(id));
        bancaRepository.delete(banca);
    }
}
