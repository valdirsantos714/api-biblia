package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.services.LivrosService;
import com.valdirsantos714.biblia.services.VersosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/livros")
@CrossOrigin("*")
@Tag(name = "Livros", description = "Endpoints para gerenciar e consultar livros bíblicos")
public class LivrosController {

    @Autowired
    private LivrosService livrosService;

    @Autowired
    private VersosService versosService;

    @GetMapping("/all")
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista completa de todos os livros bíblicos disponíveis com suas informações de testamento")
    @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivrosDTO.class)))
    public ResponseEntity<List<LivrosDTO>> findAll() {
        log.info("Executando método findAll() em LivrosController");
        List<LivrosDTO> list = livrosService.listar();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id_livro}/{capitulo}")
    @Operation(summary = "Buscar versos por ID do livro e capítulo na versão padrão NVT", description = "Retorna todos os versos de um capítulo específico de um livro identificado pelo ID, na versão padrão (NVT)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro ou capítulo não encontrado")
    })
    public ResponseEntity<List<VersosDTO>> mostraVersos(
            @Parameter(description = "ID único do livro bíblico", example = "1")
            @PathVariable(name = "id_livro") Long id_livro,
            @Parameter(description = "Número do capítulo", example = "1")
            @PathVariable(name = "capitulo") Integer capitulo) {
        log.info("Executando método mostraVersos() em LivrosController para livro ID: {} e capítulo: {}", id_livro, capitulo);
        List<VersosDTO> versos = versosService.buscarVersosPorCapitulo(id_livro, capitulo);
        return ResponseEntity.ok().body(versos);
    }

    @GetMapping("/buscarPorNome/{nomeLivro}/{capitulo}")
    @Operation(summary = "Buscar versos por nome do livro e capítulo na versão padrão NVT", description = "Retorna todos os versos de um capítulo específico identificando o livro pelo nome, na versão padrão (NVT)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro ou capítulo não encontrado")
    })
    public ResponseEntity<List<VersosDTO>> mostraVersosDeUmLivroPorNome(
            @Parameter(description = "Nome completo ou abreviação do livro", example = "Gênesis")
            @PathVariable(name = "nomeLivro") String nomeLivro,
            @Parameter(description = "Número do capítulo", example = "1")
            @PathVariable(name = "capitulo") Integer capitulo) {
        String versaoPadrao = "nvt";
        log.info("Executando método mostraVersosDeUmLivroPorNome() em LivrosController para livro: {} e capítulo: {} na versão: {}", nomeLivro, capitulo, versaoPadrao);
        List<VersosDTO> versos = versosService.buscarVersosPorLivroNomeEVersao(nomeLivro, capitulo, versaoPadrao);

        if (versos.isEmpty()) {
            log.info("Nenhum verso encontrado para o livro: {}", nomeLivro);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }

        log.info("Foram encontrados {} versos para o livro: {} e capítulo: {}", versos.size(), nomeLivro, capitulo);
        return ResponseEntity.ok().body(versos);
    }

    @GetMapping("/{livro}/{capitulo}/verNumeroDeVersos")
    @Operation(summary = "Buscar números de versos por ID do livro e capítulo", description = "Retorna uma lista com os números dos versos existentes em um capítulo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Números de versos retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro ou capítulo não encontrado")
    })
    public ResponseEntity<List<Integer>> verNumeroDeVersos(
            @Parameter(description = "ID único do livro bíblico", example = "1")
            @PathVariable(name = "livro") Long idLivro,
            @Parameter(description = "Número do capítulo", example = "1")
            @PathVariable(name = "capitulo") Integer capitulo) {
        log.info("Executando método verNumeroDeVersos() em LivrosController para livro de Id: {} e capítulo: {}", idLivro, capitulo);
        List<Integer> numeroDeVersiculos = versosService.buscarNumerosDeVersos(idLivro, capitulo);
        return ResponseEntity.ok().body(numeroDeVersiculos);
    }

    @GetMapping("/{nomeVersao}/{id_livro}/{capitulo}")
    @Operation(summary = "Buscar versos de um capítulo por versão bíblica e id do livro", description = "Retorna versos de um capítulo específico em uma versão bíblica escolhida (ex: acf, kjv, nvt)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro, capítulo ou versão não encontrado")
    })
    public ResponseEntity<List<VersosDTO>> mostraVersosPorVersao(
            @Parameter(description = "Nome da versão bíblica (acf, kjv, nvt, etc)", example = "acf")
            @PathVariable(name = "nomeVersao") String nomeVersao,
            @Parameter(description = "ID único do livro bíblico", example = "1")
            @PathVariable(name = "id_livro") Long id_livro,
            @Parameter(description = "Número do capítulo", example = "1")
            @PathVariable(name = "capitulo") Integer capitulo) {
        log.info("Executando método mostraVersosPorVersao() em LivrosController para livro ID: {}, capítulo: {} e versão: {}", id_livro, capitulo, nomeVersao);
        List<VersosDTO> versos = versosService.buscarVersosPorCapituloEVersao(id_livro, capitulo, nomeVersao);

        if (versos.isEmpty()) {
            log.info("Nenhum verso encontrado para o livro de Id: {}, capítulo: {} e versão: {}", id_livro, capitulo, nomeVersao);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        log.info("Foram encontrados {} versos para o livro de Id: {}, capítulo: {} e versão: {}", versos.size(), id_livro, capitulo, nomeVersao);
        return ResponseEntity.ok().body(versos);
    }

    @GetMapping("/buscaDetalhada/{nomeVersao}/{livro}/{capitulo}")
    @Operation(summary = "Busca detalhada de versos por nome de livro e versão", description = "Retorna versos de um capítulo específico buscando o livro pelo nome e escolhendo uma versão bíblica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro, capítulo ou versão não encontrado")
    })
    public ResponseEntity<List<VersosDTO>> mostraVersosPorVersaoELivro(
            @Parameter(description = "Nome da versão bíblica (acf, kjv, nvt, etc)", example = "acf")
            @PathVariable(name = "nomeVersao") String nomeVersao,
            @Parameter(description = "Nome completo ou abreviação do livro", example = "Gênesis")
            @PathVariable(name = "livro") String livro,
            @Parameter(description = "Número do capítulo", example = "1")
            @PathVariable(name = "capitulo") Integer capitulo
    ) {
        log.info("Executando mostraVersosPorVersaoELivro() - livro: {}, capítulo: {}, versão: {}", livro, capitulo, nomeVersao);

        List<VersosDTO> versos = versosService.buscarVersosPorLivroNomeEVersao(livro, capitulo, nomeVersao);

        if (versos.isEmpty()) {
            log.info("Nenhum verso encontrado para o livro: {}, capítulo: {}, versão: {}", livro, capitulo, nomeVersao);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }

        return ResponseEntity.ok(versos);
    }

    @GetMapping("/{nomeLivro}/capitulos")
    @Operation(summary = "Listar capítulos de um livro", description = "Retorna uma lista com todos os números de capítulos disponíveis para um livro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capítulos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<List<Integer>> listarCapitulos(
            @Parameter(description = "Nome completo ou abreviação do livro", example = "Gênesis")
            @PathVariable(name = "nomeLivro") String nomeLivro
    ) {
        log.info("Executando listarCapitulos() - livro: {}", nomeLivro);
        List<Integer> capitulos = versosService.buscarCapitulosPorLivro(nomeLivro);

        if (capitulos.isEmpty()) {
            log.info("Nenhum capítulo encontrado para o livro: {}", nomeLivro);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }

        log.info("Foram encontrados {} capítulos para o livro: {}", capitulos.size(), nomeLivro);
        return ResponseEntity.ok(capitulos);
    }

}

