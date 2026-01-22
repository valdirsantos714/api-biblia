package com.valdirsantos714.biblia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detalhes completos da busca de um verso com comparações entre todas as versões disponíveis")
public class DetalhesBuscaVerso {
    @Schema(description = "Nome do livro bíblico", example = "Gênesis")
    private String nomeLivro;

    @Schema(description = "Número do capítulo", example = "1")
    private Integer capitulo;

    @Schema(description = "Número do versículo", example = "1")
    private Integer versiculo;

    @Schema(description = "Lista de comparações do verso em diferentes versões bíblicas")
    private List<VersoComparacoesDTO> comparacoes;
}
