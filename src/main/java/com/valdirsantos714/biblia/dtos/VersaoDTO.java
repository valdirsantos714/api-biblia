package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representação de uma versão bíblica")
public class VersaoDTO {
    @Schema(description = "Identificador único da versão", example = "1")
    private Long id;

    @Schema(description = "Sigla da versão bíblica", example = "NVT")
    private String nome;
}

