package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.dtos.TestamentoDTO;
import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.services.LivrosService;
import com.valdirsantos714.biblia.services.VersosService;
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

    @Mock
    private VersosService versosService;

    @InjectMocks
    private LivrosController livrosController;

    private LivrosDTO livrosDTO;
    private VersosDTO versosDTO1;
    private VersosDTO versosDTO2;

    @BeforeEach
    void setUp() {
        livrosDTO = LivrosDTO.builder()
                .id(1L)
                .nome("Gênesis")
                .abreviacao("Gn")
                .testamento(TestamentoDTO.builder().id(1L).nome("Antigo Testamento").build())
                .build();

        versosDTO1 = VersosDTO.builder()
                .id(1L)
                .capitulo(1)
                .versiculo(1)
                .texto("No princípio, criou Deus os céus e a terra.")
                .build();

        versosDTO2 = VersosDTO.builder()
                .id(2L)
                .capitulo(1)
                .versiculo(2)
                .texto("E a terra era sem forma e vazia.")
                .build();
    }

    @Test
    @DisplayName("Given livros exist when findAll then return 200 with list")
    void testFindAll() {
        List<LivrosDTO> livrosList = new ArrayList<>();
        livrosList.add(livrosDTO);

        when(livrosService.listar()).thenReturn(livrosList);

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

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

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given multiple livros exist when findAll then return all")
    void testFindAllMultiplos() {
        LivrosDTO livro2 = LivrosDTO.builder()
                .id(2L)
                .nome("Êxodo")
                .abreviacao("Ex")
                .testamento(TestamentoDTO.builder().id(1L).nome("Antigo Testamento").build())
                .build();

        List<LivrosDTO> livrosList = new ArrayList<>();
        livrosList.add(livrosDTO);
        livrosList.add(livro2);

        when(livrosService.listar()).thenReturn(livrosList);

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

        assertEquals(2, response.getBody().size());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given livro id and capitulo exist when mostraVersos then return 200 with verses")
    void testMostraVersos() {
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2);

        when(versosService.buscarVersosPorCapitulo(1L, 1)).thenReturn(versos);

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersos(1L, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(versosService, times(1)).buscarVersosPorCapitulo(1L, 1);
    }

    @Test
    @DisplayName("Given capitulo does not have verses when mostraVersos then return empty list")
    void testMostraVersosCapituloVazio() {
        when(versosService.buscarVersosPorCapitulo(1L, 99)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersos(1L, 99);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(versosService, times(1)).buscarVersosPorCapitulo(1L, 99);
    }

    @Test
    @DisplayName("Given livro name and chapter exist when mostraVersosDeUmLivroPorNome then return 200")
    void testMostraVersosDeUmLivroPorNome() {
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2);

        when(versosService.buscarVersosPorLivroNomeEVersao("Gênesis", 1, "nvt")).thenReturn(versos);

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosDeUmLivroPorNome("Gênesis", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(versosService, times(1)).buscarVersosPorLivroNomeEVersao("Gênesis", 1, "nvt");
    }

    @Test
    @DisplayName("Given livro name not found when mostraVersosDeUmLivroPorNome then return 404")
    void testMostraVersosDeUmLivroPorNomeNotFound() {
        when(versosService.buscarVersosPorLivroNomeEVersao("Inexistente", 1, "nvt")).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosDeUmLivroPorNome("Inexistente", 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Given livro id and capitulo exist when verNumeroDeVersos then return list of numbers")
    void testVerNumeroDeVersos() {
        List<Integer> numeroDeVersiculos = List.of(1, 2, 3, 4, 5);

        when(versosService.buscarNumerosDeVersos(1L, 1)).thenReturn(numeroDeVersiculos);

        ResponseEntity<List<Integer>> response = livrosController.verNumeroDeVersos(1L, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
        verify(versosService, times(1)).buscarNumerosDeVersos(1L, 1);
    }

    @Test
    @DisplayName("Given capitulo without verses when verNumeroDeVersos then return empty list")
    void testVerNumeroDeVersosCapituloVazio() {
        when(versosService.buscarNumerosDeVersos(1L, 99)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Integer>> response = livrosController.verNumeroDeVersos(1L, 99);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Given livro id, capitulo and versao exist when mostraVersosPorVersao then return 200")
    void testMostraVersosPorVersao() {
        List<VersosDTO> versos = List.of(versosDTO1);

        when(versosService.buscarVersosPorCapituloEVersao(1L, 1, "acf")).thenReturn(versos);

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosPorVersao("acf", 1L, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(versosService, times(1)).buscarVersosPorCapituloEVersao(1L, 1, "acf");
    }

    @Test
    @DisplayName("Given verses not found for version when mostraVersosPorVersao then return 404")
    void testMostraVersosPorVersaoNotFound() {
        when(versosService.buscarVersosPorCapituloEVersao(1L, 1, "versaoInexistente")).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosPorVersao("versaoInexistente", 1L, 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Given livro name, capitulo and versao exist when mostraVersosPorVersaoELivro then return 200")
    void testMostraVersosPorVersaoELivro() {
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2);

        when(versosService.buscarVersosPorLivroNomeEVersao("Gênesis", 1, "acf")).thenReturn(versos);

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosPorVersaoELivro("acf", "Gênesis", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(versosService, times(1)).buscarVersosPorLivroNomeEVersao("Gênesis", 1, "acf");
    }

    @Test
    @DisplayName("Given livro not found when mostraVersosPorVersaoELivro then return 404")
    void testMostraVersosPorVersaoELivroNotFound() {
        when(versosService.buscarVersosPorLivroNomeEVersao("Inexistente", 1, "acf")).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosPorVersaoELivro("acf", "Inexistente", 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Given livro name exists when listarCapitulos then return list of chapters")
    void testListarCapitulos() {
        List<Integer> capitulos = List.of(1, 2, 3, 4, 5);

        when(versosService.buscarCapitulosPorLivro("Gênesis")).thenReturn(capitulos);

        ResponseEntity<List<Integer>> response = livrosController.listarCapitulos("Gênesis");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
        verify(versosService, times(1)).buscarCapitulosPorLivro("Gênesis");
    }

    @Test
    @DisplayName("Given livro not found when listarCapitulos then return 404")
    void testListarCapitulosNotFound() {
        when(versosService.buscarCapitulosPorLivro("Inexistente")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Integer>> response = livrosController.listarCapitulos("Inexistente");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Given service throws exception when findAll then propagate exception")
    void testFindAllComExcecao() {
        when(livrosService.listar()).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> livrosController.findAll());
        verify(livrosService, times(1)).listar();
    }

    @Test
    @DisplayName("Given service throws exception when mostraVersos then propagate exception")
    void testMostraVersosComExcecao() {
        when(versosService.buscarVersosPorCapitulo(1L, 1)).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> livrosController.mostraVersos(1L, 1));
        verify(versosService, times(1)).buscarVersosPorCapitulo(1L, 1);
    }

    @Test
    @DisplayName("Given service throws exception when listarCapitulos then propagate exception")
    void testListarCapitulosComExcecao() {
        when(versosService.buscarCapitulosPorLivro("Gênesis")).thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class, () -> livrosController.listarCapitulos("Gênesis"));
    }

    @Test
    @DisplayName("Should verify response entity has correct status code")
    void testVerifyResponseStatus() {
        when(livrosService.listar()).thenReturn(List.of(livrosDTO));

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return correct data in response body")
    void testResponseBodyContent() {
        when(livrosService.listar()).thenReturn(List.of(livrosDTO));

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

        assertEquals(1, response.getBody().size());
        assertEquals("Gênesis", response.getBody().get(0).getNome());
        assertEquals("Gn", response.getBody().get(0).getAbreviacao());
    }

    @Test
    @DisplayName("Should handle multiple verses in single chapter")
    void testMostraVersosMultiplos() {
        VersosDTO verso3 = VersosDTO.builder()
                .id(3L)
                .capitulo(1)
                .versiculo(3)
                .texto("E disse Deus: Haja luz.")
                .build();
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2, verso3);

        when(versosService.buscarVersosPorCapitulo(1L, 1)).thenReturn(versos);

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersos(1L, 1);

        assertEquals(3, response.getBody().size());
    }

    @Test
    @DisplayName("Should verify correct service method called with correct parameters")
    void testVerifyServiceCallParameters() {
        when(versosService.buscarVersosPorCapitulo(5L, 10)).thenReturn(new ArrayList<>());

        livrosController.mostraVersos(5L, 10);

        verify(versosService, times(1)).buscarVersosPorCapitulo(5L, 10);
        verifyNoMoreInteractions(versosService);
    }

    @Test
    @DisplayName("Should call findAll multiple times and verify consistency")
    void testMultipleCallsConsistency() {
        when(livrosService.listar()).thenReturn(List.of(livrosDTO));

        livrosController.findAll();
        livrosController.findAll();
        livrosController.findAll();

        verify(livrosService, times(3)).listar();
    }

    @Test
    @DisplayName("Should handle different chapters with different verse counts")
    void testDifferentChaptersVerseCounts() {
        List<VersosDTO> chapter1Verses = List.of(versosDTO1, versosDTO2);
        List<VersosDTO> chapter2Verses = List.of(versosDTO1);

        when(versosService.buscarVersosPorCapitulo(1L, 1)).thenReturn(chapter1Verses);
        when(versosService.buscarVersosPorCapitulo(1L, 2)).thenReturn(chapter2Verses);

        ResponseEntity<List<VersosDTO>> response1 = livrosController.mostraVersos(1L, 1);
        ResponseEntity<List<VersosDTO>> response2 = livrosController.mostraVersos(1L, 2);

        assertEquals(2, response1.getBody().size());
        assertEquals(1, response2.getBody().size());
    }

    @Test
    @DisplayName("Should preserve verse text data in response")
    void testPreserveVerseTextData() {
        List<VersosDTO> versos = List.of(versosDTO1);

        when(versosService.buscarVersosPorCapitulo(1L, 1)).thenReturn(versos);

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersos(1L, 1);

        assertNotNull(response.getBody().get(0).getTexto());
        assertEquals("No princípio, criou Deus os céus e a terra.", response.getBody().get(0).getTexto());
    }

    @Test
    @DisplayName("Should handle book name search with different versions")
    void testBookNameSearchDifferentVersions() {
        List<VersosDTO> versesNvt = List.of(versosDTO1);
        List<VersosDTO> versesAcf = List.of(versosDTO2);

        when(versosService.buscarVersosPorLivroNomeEVersao("Gênesis", 1, "nvt")).thenReturn(versesNvt);
        when(versosService.buscarVersosPorLivroNomeEVersao("Gênesis", 1, "acf")).thenReturn(versesAcf);

        ResponseEntity<List<VersosDTO>> responseNvt = livrosController.mostraVersosDeUmLivroPorNome("Gênesis", 1);
        ResponseEntity<List<VersosDTO>> responseAcf = livrosController.mostraVersosPorVersaoELivro("acf", "Gênesis", 1);

        assertEquals(1, responseNvt.getBody().size());
        assertEquals(1, responseAcf.getBody().size());
    }

    @Test
    @DisplayName("Should verify repository calls with correct chapter numbers")
    void testVerifyRepositoryCallsChapterNumbers() {
        when(versosService.buscarNumerosDeVersos(1L, 5)).thenReturn(List.of(1, 2, 3));

        livrosController.verNumeroDeVersos(1L, 5);

        verify(versosService, times(1)).buscarNumerosDeVersos(1L, 5);
    }

    @Test
    @DisplayName("Should handle empty book list")
    void testFindAllEmptyList() {
        when(livrosService.listar()).thenReturn(Collections.emptyList());

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

        assertTrue(response.getBody().isEmpty());
        assertEquals(0, response.getBody().size());
    }

    @Test
    @DisplayName("Should handle large list of books")
    void testFindAllLargeList() {
        List<LivrosDTO> largeList = new ArrayList<>();
        for (int i = 1; i <= 66; i++) {
            largeList.add(LivrosDTO.builder()
                    .id((long) i)
                    .nome("Livro " + i)
                    .abreviacao("L" + i)
                    .testamento(TestamentoDTO.builder().id((long) i).nome("Testamento").build())
                    .build());
        }

        when(livrosService.listar()).thenReturn(largeList);

        ResponseEntity<List<LivrosDTO>> response = livrosController.findAll();

        assertEquals(66, response.getBody().size());
    }

    @Test
    @DisplayName("Should handle search by version with no results")
    void testSearchByVersionNoResults() {
        when(versosService.buscarVersosPorCapituloEVersao(1L, 1, "nonexistent")).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersosDTO>> response = livrosController.mostraVersosPorVersao("nonexistent", 1L, 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
