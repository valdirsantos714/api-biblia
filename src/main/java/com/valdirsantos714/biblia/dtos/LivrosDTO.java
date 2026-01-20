package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representação de um livro bíblico com seus dados básicos")
public class LivrosDTO {
    @Schema(description = "Identificador único do livro", example = "1")
    private Long id;

    @Schema(description = "Nome completo do livro bíblico", example = "Gênesis")
    private String nome;

    @Schema(description = "Abreviação do nome do livro", example = "Gn")
    private String abreviacao;

    @Schema(description = "Testamento ao qual o livro pertence")
    private TestamentoDTO testamento;
}

