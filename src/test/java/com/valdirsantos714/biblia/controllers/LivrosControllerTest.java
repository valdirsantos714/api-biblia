package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.entities.biblia.Testamento;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import com.valdirsantos714.biblia.services.LivrosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LivrosController Comprehensive Tests")
class LivrosControllerTest {

    @Mock
    private LivrosService livrosService;

    @InjectMocks
    private LivrosController livrosController;

    private Livros livro;
    private Testamento testamento;
    private Versos verso1;
    private Versos verso2;
    private Versos verso3;

    @BeforeEach
    void setUp() {
        testamento = new Testamento();
        testamento.setId(1L);
        testamento.setNome("Antigo Testamento");

        livro = new Livros();
        livro.setId(1L);
        livro.setNome("Gênesis");
        livro.setAbreviacao("Gn");
        livro.setTestamento(testamento);

        verso1 = new Versos();
        verso1.setId(1L);
        verso1.setCapitulo(1);
        verso1.setVersiculo(1);
        verso1.setTexto("No princípio, criou Deus os céus e a terra.");
        verso1.setLivro(livro);

        verso2 = new Versos();
        verso2.setId(2L);
        verso2.setCapitulo(1);
        verso2.setVersiculo(2);
        verso2.setTexto("E a terra era sem forma e vazia.");
        verso2.setLivro(livro);

        verso3 = new Versos();
        verso3.setId(3L);
        verso3.setCapitulo(2);
        verso3.setVersiculo(1);
        verso3.setTexto("E consumaram-se os céus e a terra.");
        verso3.setLivro(livro);

        Set<Versos> versos = new HashSet<>();
        versos.add(verso1);
        versos.add(verso2);
        versos.add(verso3);
        livro.setListVersos(versos);
    }

    @Test
    @DisplayName("Given livros exist when findAll then return 200 with list")
    void testFindAll() {
        List<Livros> livrosList = new ArrayList<>();
        livrosList.add(livro);

        when(livrosService.listar()).thenReturn(livrosList);

        ResponseEntity<List<Livros>> response = livrosController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Gênesis", response.getBody().get(0).getNome());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given no livros exist when findAll then return 200 with empty list")
    void testFindAllEmpty() {
        when(livrosService.listar()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Livros>> response = livrosController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given multiple livros exist when findAll then return all")
    void testFindAllMultiplos() {
        Livros livro2 = new Livros();
        livro2.setId(2L);
        livro2.setNome("Êxodo");

        List<Livros> livrosList = new ArrayList<>();
        livrosList.add(livro);
        livrosList.add(livro2);

        when(livrosService.listar()).thenReturn(livrosList);

        ResponseEntity<List<Livros>> response = livrosController.findAll();

        assertEquals(2, response.getBody().size());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given livro id and capitulo exist when amostraVersos then return 200 with verses")
    void testAmostraVersos() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Versos>> response = livrosController.amostraVersos(1L, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given capitulo does not have verses when amostraVersos then return empty set")
    void testAmostraVersosCapituloVazio() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Versos>> response = livrosController.amostraVersos(1L, 99);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given different chapters when amostraVersos then filter correctly")
    void testAmostraVersosCapitulosDiferentes() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Versos>> response1 = livrosController.amostraVersos(1L, 1);
        ResponseEntity<Set<Versos>> response2 = livrosController.amostraVersos(1L, 2);

        assertEquals(2, response1.getBody().size());
        assertEquals(1, response2.getBody().size());
        verify(livrosService, times(2)).findById(1L);
    }

    @Test
    @DisplayName("Given livro id exists when mostraNumeroDeCapitulosPeloId then return 200 with chapters")
    void testMostraNumeroDeCapitulosPeloId() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Integer>> response = livrosController.mostraNumeroDeCapitulosPeloId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(1));
        assertTrue(response.getBody().contains(2));
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given livro id when mostraNumeroDeCapitulosPeloId then call service once")
    void testMostraNumeroDeCapitulosVerificaRepositorio() {
        when(livrosService.findById(1L)).thenReturn(livro);

        livrosController.mostraNumeroDeCapitulosPeloId(1L);

        verify(livrosService, times(1)).findById(1L);
        verifyNoMoreInteractions(livrosService);
    }

    @Test
    @DisplayName("Given livro id and capitulo exist when verNumeroDeVersos then return 200 with verses count")
    void testVerNumeroDeVersos() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<List<Integer>> response = livrosController.verNumeroDeVersos(1L, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(1));
        assertTrue(response.getBody().contains(2));
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given capitulo without verses when verNumeroDeVersos then return empty list")
    void testVerNumeroDeVersosCapituloVazio() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<List<Integer>> response = livrosController.verNumeroDeVersos(1L, 99);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given different chapters when verNumeroDeVersos then return correct verses")
    void testVerNumeroDeVersosMultiplosCapitulos() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<List<Integer>> response1 = livrosController.verNumeroDeVersos(1L, 1);
        ResponseEntity<List<Integer>> response2 = livrosController.verNumeroDeVersos(1L, 2);

        assertEquals(2, response1.getBody().size());
        assertEquals(1, response2.getBody().size());
        assertEquals(1, response2.getBody().get(0).intValue());
        verify(livrosService, times(2)).findById(1L);
    }

    @Test
    @DisplayName("Given livro id and capitulo exist when amostraVersos then verify response type")
    void testAmostraVersosResponseType() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Versos>> response = livrosController.amostraVersos(1L, 1);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Set);
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given livro with multiple verses when amostraVersos then preserve verse data")
    void testAmostraVersosPreserveData() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Versos>> response = livrosController.amostraVersos(1L, 1);

        Set<Versos> verses = response.getBody();
        verses.forEach(verso -> {
            assertEquals(1, verso.getCapitulo());
            assertNotNull(verso.getTexto());
            assertNotNull(verso.getId());
        });
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given service throws exception when findAll then propagate exception")
    void testFindAllComExcecao() {
        when(livrosService.listar()).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> livrosController.findAll());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given service throws exception when amostraVersos then propagate exception")
    void testAmostraVersosComExcecao() {
        when(livrosService.findById(1L)).thenThrow(new RuntimeException("Book not found"));

        assertThrows(RuntimeException.class, () -> livrosController.amostraVersos(1L, 1));
        verify(livrosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given service throws exception when mostraNumeroDeCapitulosPeloId then propagate exception")
    void testMostraNumeroDeCapitulosComExcecao() {
        when(livrosService.findById(99L)).thenThrow(new RuntimeException("Book not found"));

        assertThrows(RuntimeException.class, () -> livrosController.mostraNumeroDeCapitulosPeloId(99L));
        verify(livrosService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Given service throws exception when verNumeroDeVersos then propagate exception")
    void testVerNumeroDeVersosComExcecao() {
        when(livrosService.findById(99L)).thenThrow(new RuntimeException("Book not found"));

        assertThrows(RuntimeException.class, () -> livrosController.verNumeroDeVersos(99L, 1));
        verify(livrosService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Given livro with capitulo 1 when multiple calls then return consistent results")
    void testConsistentResults() {
        when(livrosService.findById(1L)).thenReturn(livro);

        ResponseEntity<Set<Versos>> response1 = livrosController.amostraVersos(1L, 1);
        ResponseEntity<Set<Versos>> response2 = livrosController.amostraVersos(1L, 1);

        assertEquals(response1.getBody().size(), response2.getBody().size());
        verify(livrosService, times(2)).findById(1L);
    }
}

