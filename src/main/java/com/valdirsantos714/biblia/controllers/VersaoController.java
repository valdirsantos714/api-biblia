package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.dtos.DetalhesBuscaVerso;
import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.services.VersaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/versoes")
@CrossOrigin("*")
@Tag(name = "Versões", description = "Endpoints para gerenciar e comparar versões bíblicas")
public class VersaoController {

    @Autowired
    private VersaoService versaoService;

    @GetMapping("/all")
    @Operation(summary = "Listar todas as versões bíblicas", description = "Retorna uma lista completa de todas as versões bíblicas disponíveis no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de versões retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersaoDTO.class)))
    public ResponseEntity<List<VersaoDTO>> listarTodas() {
        log.info("Executando método listarTodas() em VersaoController");
        List<VersaoDTO> versoes = versaoService.listarTodasAsVersoes();
        return ResponseEntity.ok().body(versoes);
    }

    @GetMapping("/compare/{nomeLivro}/{capitulo}/{versiculo}")
    @Operation(summary = "Comparar verso em todas as versões",
            description = "Retorna o mesmo versículo de um livro específico em todas as versões bíblicas disponíveis, permitindo comparação de diferentes traduções")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verso encontrado em todas as versões com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetalhesBuscaVerso.class))),
            @ApiResponse(responseCode = "404", description = "Verso não encontrado nas versões disponíveis"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos fornecidos")
    })
    public ResponseEntity<DetalhesBuscaVerso> compararVerso(
            @Parameter(description = "Nome do livro bíblico", example = "Gênesis", required = true)
            @PathVariable(name = "nomeLivro") String nomeLivro,
            @Parameter(description = "Número do capítulo", example = "1", required = true)
            @PathVariable(name = "capitulo") Integer capitulo,
            @Parameter(description = "Número do versículo", example = "1", required = true)
            @PathVariable(name = "versiculo") Integer versiculo) {
        log.info("Executando método compararVerso() em VersaoController para livro: {}, capítulo: {}, versículo: {}",
                nomeLivro, capitulo, versiculo);
        DetalhesBuscaVerso detalhesBuscaVerso = versaoService.compararVersoEmTodasAsVersoes(nomeLivro, capitulo, versiculo);
        return ResponseEntity.ok().body(detalhesBuscaVerso);
    }
}

