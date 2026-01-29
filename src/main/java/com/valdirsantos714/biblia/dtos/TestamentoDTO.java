package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representação de um testamento bíblico (Velho ou Novo)")
public class TestamentoDTO {
    @Schema(description = "Identificador único do testamento", example = "1")
    private Long id;

    @Schema(description = "Nome do testamento", example = "Velho Testamento")
    private String nome;
}
