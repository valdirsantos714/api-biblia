package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.repositories.VersosRepository;
import com.valdirsantos714.biblia.utils.TextoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VersosService Tests")
class VersosServiceTest {

    @Mock
    private VersosRepository versosRepository;

    @InjectMocks
    private VersosService versosService;

    private VersosDTO versosDTO1;
    private VersosDTO versosDTO2;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Should find verses by livro id and chapter")
    void testBuscarVersosPorCapitulo() {
        Long idLivro = 1L;
        Integer capitulo = 1;
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2);

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt"))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorCapitulo(idLivro, capitulo);

        assertEquals(2, resultado.size());
        assertEquals("No princípio, criou Deus os céus e a terra.", resultado.get(0).getTexto());
        verify(versosRepository, times(1)).findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt");
    }

    @Test
    @DisplayName("Should return empty list when no verses found")
    void testBuscarVersosPorCapituloVazio() {
        Long idLivro = 99L;
        Integer capitulo = 999;

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt"))
                .thenReturn(new ArrayList<>());

        List<VersosDTO> resultado = versosService.buscarVersosPorCapitulo(idLivro, capitulo);

        assertTrue(resultado.isEmpty());
        verify(versosRepository, times(1)).findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt");
    }

    @Test
    @DisplayName("Should find verse numbers by livro id and chapter")
    void testBuscarNumerosDeVersos() {
        Long idLivro = 1L;
        Integer capitulo = 1;
        List<Integer> numeros = List.of(1, 2, 3);

        when(versosRepository.findVersiculosByLivroIdAndCapitulo(idLivro, capitulo))
                .thenReturn(numeros);

        List<Integer> resultado = versosService.buscarNumerosDeVersos(idLivro, capitulo);

        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(1));
        verify(versosRepository, times(1)).findVersiculosByLivroIdAndCapitulo(idLivro, capitulo);
    }

    @Test
    @DisplayName("Should return empty list when no verse numbers found")
    void testBuscarNumerosDeVersosVazio() {
        Long idLivro = 99L;
        Integer capitulo = 999;

        when(versosRepository.findVersiculosByLivroIdAndCapitulo(idLivro, capitulo))
                .thenReturn(new ArrayList<>());

        List<Integer> resultado = versosService.buscarNumerosDeVersos(idLivro, capitulo);

        assertTrue(resultado.isEmpty());
        verify(versosRepository, times(1)).findVersiculosByLivroIdAndCapitulo(idLivro, capitulo);
    }

    @Test
    @DisplayName("Should find verses by livro id, chapter and version")
    void testBuscarVersosPorCapituloEVersao() {
        Long idLivro = 1L;
        Integer capitulo = 1;
        String versao = "acf";
        List<VersosDTO> versos = List.of(versosDTO1);

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, versao))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorCapituloEVersao(idLivro, capitulo, versao);

        assertEquals(1, resultado.size());
        verify(versosRepository, times(1)).findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, versao);
    }

    @Test
    @DisplayName("Should find verses by book name and version")
    void testBuscarVersosPorLivroNomeEVersao() {
        String livro = "Gênesis";
        Integer capitulo = 1;
        String versao = "nvt";
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2);
        String livroNormalizado = TextoUtils.normalizar(livro);

        when(versosRepository.findByLivroNomeOuAbreviacaoAndCapituloAndVersao(livroNormalizado, capitulo, versao))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorLivroNomeEVersao(livro, capitulo, versao);

        assertEquals(2, resultado.size());
        verify(versosRepository, times(1)).findByLivroNomeOuAbreviacaoAndCapituloAndVersao(livroNormalizado, capitulo, versao);
    }

    @Test
    @DisplayName("Should find chapters by book name")
    void testBuscarCapitulosPorLivro() {
        String livro = "Gênesis";
        List<Integer> capitulos = List.of(1, 2, 3, 4, 5);
        String livroNormalizado = TextoUtils.normalizar(livro);

        when(versosRepository.findDistinctCapitulosByLivroNomeOuAbreviacao(livroNormalizado))
                .thenReturn(capitulos);

        List<Integer> resultado = versosService.buscarCapitulosPorLivro(livro);

        assertEquals(5, resultado.size());
        assertTrue(resultado.contains(1));
        assertTrue(resultado.contains(5));
        verify(versosRepository, times(1)).findDistinctCapitulosByLivroNomeOuAbreviacao(livroNormalizado);
    }

    @Test
    @DisplayName("Should return empty list when no chapters found")
    void testBuscarCapitulosPorLivroVazio() {
        String livro = "Livro Inexistente";
        String livroNormalizado = TextoUtils.normalizar(livro);

        when(versosRepository.findDistinctCapitulosByLivroNomeOuAbreviacao(livroNormalizado))
                .thenReturn(new ArrayList<>());

        List<Integer> resultado = versosService.buscarCapitulosPorLivro(livro);

        assertTrue(resultado.isEmpty());
        verify(versosRepository, times(1)).findDistinctCapitulosByLivroNomeOuAbreviacao(livroNormalizado);
    }

    @Test
    @DisplayName("Should handle multiple verses in chapter")
    void testBuscarVersosPorCapituloMultiplos() {
        Long idLivro = 1L;
        Integer capitulo = 1;
        VersosDTO verso3 = VersosDTO.builder()
                .id(3L)
                .capitulo(1)
                .versiculo(3)
                .texto("E disse Deus: Haja luz.")
                .build();
        List<VersosDTO> versos = List.of(versosDTO1, versosDTO2, verso3);

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt"))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorCapitulo(idLivro, capitulo);

        assertEquals(3, resultado.size());
        assertEquals(3, resultado.get(2).getVersiculo());
        verify(versosRepository, times(1)).findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt");
    }

    @Test
    @DisplayName("Should preserve verse text when fetching")
    void testBuscarVersosPorCapituloPreserveData() {
        Long idLivro = 1L;
        Integer capitulo = 1;
        List<VersosDTO> versos = List.of(versosDTO1);

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt"))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorCapitulo(idLivro, capitulo);

        assertNotNull(resultado.get(0).getTexto());
        assertEquals("No princípio, criou Deus os céus e a terra.", resultado.get(0).getTexto());
    }

    @Test
    @DisplayName("Should normalize book name when searching")
    void testBuscarVersosPorLivroNomeEVersaoNormalizacao() {
        String livro = "GÊNESIS";
        Integer capitulo = 1;
        String versao = "nvt";
        List<VersosDTO> versos = List.of(versosDTO1);
        String livroNormalizado = TextoUtils.normalizar(livro);

        when(versosRepository.findByLivroNomeOuAbreviacaoAndCapituloAndVersao(livroNormalizado, capitulo, versao))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorLivroNomeEVersao(livro, capitulo, versao);

        assertEquals(1, resultado.size());
        verify(versosRepository, times(1)).findByLivroNomeOuAbreviacaoAndCapituloAndVersao(livroNormalizado, capitulo, versao);
    }

    @Test
    @DisplayName("Should throw exception when repository fails for verses by chapter")
    void testBuscarVersosPorCapituloComExcecao() {
        Long idLivro = 1L;
        Integer capitulo = 1;

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, "nvt"))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> versosService.buscarVersosPorCapitulo(idLivro, capitulo));
    }

    @Test
    @DisplayName("Should throw exception when repository fails for verse numbers")
    void testBuscarNumerosDeVersosComExcecao() {
        Long idLivro = 1L;
        Integer capitulo = 1;

        when(versosRepository.findVersiculosByLivroIdAndCapitulo(idLivro, capitulo))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> versosService.buscarNumerosDeVersos(idLivro, capitulo));
    }

    @Test
    @DisplayName("Should call repository with correct parameters for chapter and version")
    void testBuscarVersosPorCapituloEVersaoParametros() {
        Long idLivro = 5L;
        Integer capitulo = 10;
        String versao = "kjv";

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, versao))
                .thenReturn(new ArrayList<>());

        versosService.buscarVersosPorCapituloEVersao(idLivro, capitulo, versao);

        verify(versosRepository, times(1)).findByLivroIdAndCapituloAndVersaoNome(5L, 10, "kjv");
    }

    @Test
    @DisplayName("Should verify repository is called correct number of times")
    void testBuscarVersosPorCapituloMultipleChalls() {
        Long idLivro = 1L;
        List<VersosDTO> versos = List.of(versosDTO1);

        when(versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, 1, "nvt"))
                .thenReturn(versos);

        versosService.buscarVersosPorCapitulo(idLivro, 1);
        versosService.buscarVersosPorCapitulo(idLivro, 1);

        verify(versosRepository, times(2)).findByLivroIdAndCapituloAndVersaoNome(idLivro, 1, "nvt");
    }

    @Test
    @DisplayName("Should handle special characters in book name")
    void testBuscarVersosPorLivroNomeEVersaoCaracteresEspeciais() {
        String livro = "Gênesis";
        Integer capitulo = 1;
        String versao = "nvt";
        List<VersosDTO> versos = List.of(versosDTO1);
        String livroNormalizado = TextoUtils.normalizar(livro);

        when(versosRepository.findByLivroNomeOuAbreviacaoAndCapituloAndVersao(livroNormalizado, capitulo, versao))
                .thenReturn(versos);

        List<VersosDTO> resultado = versosService.buscarVersosPorLivroNomeEVersao(livro, capitulo, versao);

        assertEquals(1, resultado.size());
    }
}

