package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representação de um verso bíblico com seu conteúdo e metadados")
public class VersosDTO {
    @Schema(description = "Identificador único do verso", example = "1")
    private Long id;

    @Schema(description = "Versão bíblica do verso")
    private VersaoDTO versao;

    @Schema(description = "Livro ao qual o verso pertence")
    private LivrosDTO livro;

    @Schema(description = "Número do capítulo", example = "1")
    private Integer capitulo;

    @Schema(description = "Número do versículo", example = "1")
    private Integer versiculo;

    @Schema(description = "Texto completo do verso", example = "No princípio, criou Deus os céus e a terra.")
    private String texto;

    @Schema(description = "Identificador do testamento (1=Velho Testamento, 2=Novo Testamento)", example = "1")
    private Integer testamento;
}

