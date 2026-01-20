package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.services.VersiculosDoDiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/versiculos")
@Tag(name = "Versículos do Dia", description = "Endpoints para gerenciar versículos destacados do dia")
public class VersiculoDoDiaController {

    @Autowired
    private VersiculosDoDiaService versiculoDoDiaService;

    @GetMapping(value = "/all")
    @Operation(summary = "Listar todos os versículos do dia", description = "Retorna uma lista completa de todos os versículos do dia cadastrados no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de versículos do dia retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VersiculoDoDia.class)))
    public ResponseEntity<List<VersiculoDoDia>> findAllVersiculoDoDias() {
        List<VersiculoDoDia> list = versiculoDoDiaService.findAll();

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Buscar versículo do dia por ID", description = "Retorna um versículo específico do dia identificado por seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Versículo do dia encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Versículo do dia não encontrado")
    })
    public ResponseEntity<VersiculoDoDia> findByIdVersiculoDoDia(
            @Parameter(description = "ID único do versículo do dia", example = "1")
            @PathVariable Long id) {
        VersiculoDoDia versiculoDoDia = versiculoDoDiaService.findById(id);

        return ResponseEntity.ok().body(versiculoDoDia);
    }

    @PostMapping
    @Operation(summary = "Criar novo versículo do dia", description = "Cria um novo versículo do dia e o persiste no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Versículo do dia criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<VersiculoDoDia> saveVerso(
            @Parameter(description = "Dados do versículo do dia a ser criado")
            @RequestBody VersiculoDoDia versiculoDoDia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(versiculoDoDiaService.save(versiculoDoDia));
    }


}
