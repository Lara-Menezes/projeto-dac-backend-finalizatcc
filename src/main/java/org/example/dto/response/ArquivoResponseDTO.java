package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TipoArquivo;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoResponseDTO {
    private Long id;
    private String nomeArquivo;
    private String caminho;
    private TipoArquivo tipo;
    private String mimeType;
    private Long tamanho;
    private String hashArquivo;
    private LocalDateTime dataUpload;
    private Long submissaoId;
}
