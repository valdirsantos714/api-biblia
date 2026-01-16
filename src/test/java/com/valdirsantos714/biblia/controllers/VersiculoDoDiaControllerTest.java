package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.entities.biblia.Testamento;
import com.valdirsantos714.biblia.entities.biblia.Versao;
import com.valdirsantos714.biblia.services.VersiculosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VersiculoDoDiaController Comprehensive Tests")
class VersiculoDoDiaControllerTest {

    @Mock
    private VersiculosService versiculosService;

    @InjectMocks
    private VersiculoDoDiaController versiculoDoDiaController;

    private VersiculoDoDia versiculo;
    private Versos verso;
    private Livros livro;
    private Testamento testamento;
    private Versao versao;

    @BeforeEach
    void setUp() {
        testamento = new Testamento();
        testamento.setId(1L);
        testamento.setNome("Antigo Testamento");

        versao = new Versao();
        versao.setId(1L);
        versao.setNome("Almeida Corrigida");

        livro = new Livros();
        livro.setId(1L);
        livro.setNome("Gênesis");
        livro.setAbreviacao("Gn");
        livro.setTestamento(testamento);

        verso = new Versos();
        verso.setId(1L);
        verso.setCapitulo(1);
        verso.setVersiculo(1);
        verso.setTexto("No princípio, criou Deus os céus e a terra.");
        verso.setTestamento(1);
        verso.setLivro(livro);
        verso.setVersao(versao);

        versiculo = new VersiculoDoDia();
        versiculo.setId(1L);
        versiculo.setVerso(verso);
    }

    @Test
    @DisplayName("Given versiculos exist when findAllVersiculoDoDias then return 200 with list")
    void testFindAllVersiculoDoDias() {
        List<VersiculoDoDia> versiculos = new ArrayList<>();
        versiculos.add(versiculo);

        when(versiculosService.findAll()).thenReturn(versiculos);

        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        verify(versiculosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given no versiculos exist when findAllVersiculoDoDias then return 200 with empty list")
    void testFindAllVersiculoDoDiasEmpty() {
        when(versiculosService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(versiculosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given multiple versiculos exist when findAllVersiculoDoDias then return all")
    void testFindAllVersiculoDoDiasMultiplos() {
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);
        Versos verso2 = new Versos();
        verso2.setId(2L);
        verso2.setTexto("E a terra era sem forma e vazia.");
        versiculo2.setVerso(verso2);

        List<VersiculoDoDia> versiculos = new ArrayList<>();
        versiculos.add(versiculo);
        versiculos.add(versiculo2);

        when(versiculosService.findAll()).thenReturn(versiculos);

        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(versiculosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given versiculo id exists when findByIdVersiculoDoDia then return 200 with versiculo")
    void testFindByIdVersiculoDoDia() {
        when(versiculosService.findById(1L)).thenReturn(versiculo);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.findByIdVersiculoDoDia(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(verso.getId(), response.getBody().getVerso().getId());
        verify(versiculosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given versiculo id does not exist when findByIdVersiculoDoDia then throw exception")
    void testFindByIdVersiculoDoDiaNaoEncontrado() {
        when(versiculosService.findById(99L)).thenThrow(new RuntimeException("Not found"));

        assertThrows(RuntimeException.class, () -> versiculoDoDiaController.findByIdVersiculoDoDia(99L));
        verify(versiculosService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Given versiculo to save when saveVerso then return 201 with saved versiculo")
    void testSaveVerso() {
        when(versiculosService.save(versiculo)).thenReturn(versiculo);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(versiculo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(versiculosService, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given versiculo with verso when saveVerso then persist with verso")
    void testSaveVersoComVersoAssociado() {
        when(versiculosService.save(versiculo)).thenReturn(versiculo);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(versiculo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getVerso());
        assertEquals("No princípio, criou Deus os céus e a terra.", response.getBody().getVerso().getTexto());
        verify(versiculosService, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given new versiculo when saveVerso then return generated id")
    void testSaveVersoNovoVersiculo() {
        VersiculoDoDia novoVersiculo = new VersiculoDoDia();
        novoVersiculo.setVerso(verso);

        when(versiculosService.save(novoVersiculo)).thenReturn(versiculo);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(novoVersiculo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(1L, response.getBody().getId());
        verify(versiculosService, times(1)).save(novoVersiculo);
    }

    @Test
    @DisplayName("Given multiple versiculos when findAllVersiculoDoDias then return correct count")
    void testFindAllVersiculoDoDiasMultiplosComVersos() {
        List<VersiculoDoDia> versiculos = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            VersiculoDoDia v = new VersiculoDoDia();
            v.setId((long) i);
            Versos ver = new Versos();
            ver.setId((long) i);
            v.setVerso(ver);
            versiculos.add(v);
        }

        when(versiculosService.findAll()).thenReturn(versiculos);

        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();

        assertEquals(5, response.getBody().size());
        verify(versiculosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given versiculo with complete data when findByIdVersiculoDoDia then return full data")
    void testFindByIdVersiculoDoDiaComDadosCompletos() {
        when(versiculosService.findById(1L)).thenReturn(versiculo);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.findByIdVersiculoDoDia(1L);

        VersiculoDoDia resultado = response.getBody();
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getVerso().getId());
        assertEquals(1, resultado.getVerso().getCapitulo());
        assertEquals(1, resultado.getVerso().getVersiculo());
        assertEquals("Gênesis", resultado.getVerso().getLivro().getNome());
        verify(versiculosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given versiculo when saveVerso then verify service call once")
    void testSaveVersoVerificaRepositorio() {
        when(versiculosService.save(versiculo)).thenReturn(versiculo);

        versiculoDoDiaController.saveVerso(versiculo);

        verify(versiculosService, times(1)).save(versiculo);
        verifyNoMoreInteractions(versiculosService);
    }

    @Test
    @DisplayName("Given service throws exception when findAllVersiculoDoDias then propagate exception")
    void testFindAllVersiculoDoDiasComExcecao() {
        when(versiculosService.findAll()).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> versiculoDoDiaController.findAllVersiculoDoDias());
        verify(versiculosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given service throws exception when findByIdVersiculoDoDia then propagate exception")
    void testFindByIdVersiculoDoDiaComExcecao() {
        when(versiculosService.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> versiculoDoDiaController.findByIdVersiculoDoDia(1L));
        verify(versiculosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given service throws exception when saveVerso then propagate exception")
    void testSaveVersoComExcecao() {
        when(versiculosService.save(versiculo)).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> versiculoDoDiaController.saveVerso(versiculo));
        verify(versiculosService, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given multiple different versiculos when save sequentially then return each correctly")
    void testSaveMultiplosVersiculosSequencialmente() {
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);
        Versos verso2 = new Versos();
        verso2.setId(2L);
        verso2.setTexto("Novo texto");
        versiculo2.setVerso(verso2);

        when(versiculosService.save(versiculo)).thenReturn(versiculo);
        when(versiculosService.save(versiculo2)).thenReturn(versiculo2);

        ResponseEntity<VersiculoDoDia> response1 = versiculoDoDiaController.saveVerso(versiculo);
        ResponseEntity<VersiculoDoDia> response2 = versiculoDoDiaController.saveVerso(versiculo2);

        assertEquals(1L, response1.getBody().getId());
        assertEquals(2L, response2.getBody().getId());
        verify(versiculosService, times(1)).save(versiculo);
        verify(versiculosService, times(1)).save(versiculo2);
    }

    @Test
    @DisplayName("Given versiculo from different livro when findByIdVersiculoDoDia then return correct livro")
    void testFindByIdVersiculoDoDiaDiferenteLivro() {
        Livros outroLivro = new Livros();
        outroLivro.setId(2L);
        outroLivro.setNome("Êxodo");

        Versos versoOutroLivro = new Versos();
        versoOutroLivro.setId(2L);
        versoOutroLivro.setLivro(outroLivro);

        VersiculoDoDia versiculoOutroLivro = new VersiculoDoDia();
        versiculoOutroLivro.setId(2L);
        versiculoOutroLivro.setVerso(versoOutroLivro);

        when(versiculosService.findById(2L)).thenReturn(versiculoOutroLivro);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.findByIdVersiculoDoDia(2L);

        assertEquals("Êxodo", response.getBody().getVerso().getLivro().getNome());
        verify(versiculosService, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Given versiculo when saveVerso then verify response contains all data")
    void testSaveVersoVerifyResponseData() {
        when(versiculosService.save(versiculo)).thenReturn(versiculo);

        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(versiculo);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getVerso());
        assertEquals(verso.getTexto(), response.getBody().getVerso().getTexto());
    }
}

