package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representação de um verso em uma versão bíblica específica para comparação")
public class VersoComparacoesDTO {
    @Schema(description = "Nome da versão bíblica", example = "NVI")
    private String versao;

    @Schema(description = "Texto do verso nesta versão", example = "No princípio criou Deus os céus e a terra.")
    private String texto;
}
