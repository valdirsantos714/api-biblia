package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representação de um testamento bíblico (Antigo ou Novo)")
public class TestamentoDTO {
    @Schema(description = "Identificador único do testamento", example = "1")
    private Long id;

    @Schema(description = "Nome do testamento", example = "Antigo Testamento")
    private String nome;
}
