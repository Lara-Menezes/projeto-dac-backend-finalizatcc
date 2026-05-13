package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.TipoArquivo;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoRequestDTO {
    @NotBlank(message = "Nome do arquivo é obrigatório")
    private String nomeArquivo;

    @NotBlank(message = "Caminho é obrigatório")
    private String caminho;

    @NotNull(message = "Tipo é obrigatório")
    private TipoArquivo tipo;

    private String mimeType;
    private Long tamanho;
    private String hashArquivo;

    private LocalDateTime dataUpload;

    @NotNull(message = "ID da submissão é obrigatório")
    private Long submissaoId;
}
