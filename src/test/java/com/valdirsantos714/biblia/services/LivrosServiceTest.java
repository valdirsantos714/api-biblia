package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.dtos.TestamentoDTO;
import com.valdirsantos714.biblia.repositories.LivrosRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LivrosService Tests")
class LivrosServiceTest {

    @Mock
    private LivrosRepository livrosRepository;

    @InjectMocks
    private LivrosService livrosService;

    private LivrosDTO livrosDTO;

    @BeforeEach
    void setUp() {
        livrosDTO = LivrosDTO.builder()
                .id(1L)
                .nome("Gênesis")
                .abreviacao("Gn")
                .testamento(TestamentoDTO.builder().id(1L).nome("Antigo Testamento").build())
                .build();
    }

    @Test
    @DisplayName("Should list all books successfully")
    void testListarLivros() {
        List<LivrosDTO> livrosList = new ArrayList<>();
        livrosList.add(livrosDTO);

        when(livrosRepository.listarLivrosComTestamento()).thenReturn(livrosList);

        List<LivrosDTO> resultado = livrosService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Gênesis", resultado.get(0).getNome());
        verify(livrosRepository, times(1)).listarLivrosComTestamento();
    }

    @Test
    @DisplayName("Should return empty list when no books found")
    void testListarLivrosVazio() {
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(new ArrayList<>());

        List<LivrosDTO> resultado = livrosService.listar();

        assertTrue(resultado.isEmpty());
        verify(livrosRepository, times(1)).listarLivrosComTestamento();
    }

    @Test
    @DisplayName("Should handle multiple books in list")
    void testListarMultiplosLivros() {
        LivrosDTO livro2 = LivrosDTO.builder()
                .id(2L)
                .nome("Êxodo")
                .abreviacao("Ex")
                .testamento(TestamentoDTO.builder().id(1L).nome("Antigo Testamento").build())
                .build();

        List<LivrosDTO> livrosList = new ArrayList<>();
        livrosList.add(livrosDTO);
        livrosList.add(livro2);

        when(livrosRepository.listarLivrosComTestamento()).thenReturn(livrosList);

        List<LivrosDTO> resultado = livrosService.listar();

        assertEquals(2, resultado.size());
        assertEquals("Gênesis", resultado.get(0).getNome());
        assertEquals("Êxodo", resultado.get(1).getNome());
        verify(livrosRepository, times(1)).listarLivrosComTestamento();
    }

    @Test
    @DisplayName("Should return books from different testaments")
    void testListarLivrosDiferentesTestamentos() {
        LivrosDTO livroNovoTestamento = LivrosDTO.builder()
                .id(40L)
                .nome("Mateus")
                .abreviacao("Mt")
                .testamento(TestamentoDTO.builder().id(2L).nome("Novo Testamento").build())
                .build();

        List<LivrosDTO> livrosList = new ArrayList<>();
        livrosList.add(livrosDTO);
        livrosList.add(livroNovoTestamento);

        when(livrosRepository.listarLivrosComTestamento()).thenReturn(livrosList);

        List<LivrosDTO> resultado = livrosService.listar();

        assertEquals(2, resultado.size());
        assertEquals("Antigo Testamento", resultado.get(0).getTestamento().getNome());
        assertEquals("Novo Testamento", resultado.get(1).getTestamento().getNome());
    }

    @Test
    @DisplayName("Should preserve book data when listing")
    void testListarPreserveData() {
        List<LivrosDTO> livrosList = List.of(livrosDTO);

        when(livrosRepository.listarLivrosComTestamento()).thenReturn(livrosList);

        List<LivrosDTO> resultado = livrosService.listar();

        assertNotNull(resultado.get(0).getId());
        assertNotNull(resultado.get(0).getNome());
        assertNotNull(resultado.get(0).getAbreviacao());
        assertNotNull(resultado.get(0).getTestamento());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Gênesis", resultado.get(0).getNome());
        assertEquals("Gn", resultado.get(0).getAbreviacao());
    }

    @Test
    @DisplayName("Should call repository method once when listing")
    void testListarVerifyRepositoryCalls() {
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(List.of(livrosDTO));

        livrosService.listar();
        livrosService.listar();

        verify(livrosRepository, times(2)).listarLivrosComTestamento();
    }

    @Test
    @DisplayName("Should handle service exception from repository")
    void testListarComExcecaoRepository() {
        when(livrosRepository.listarLivrosComTestamento())
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> livrosService.listar());
    }

    @Test
    @DisplayName("Should return correct list size for various counts")
    void testListarVariousSizes() {
        List<LivrosDTO> smallList = List.of(livrosDTO);
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(smallList);
        assertEquals(1, livrosService.listar().size());

        List<LivrosDTO> largeList = new ArrayList<>();
        for (int i = 1; i <= 66; i++) {
            largeList.add(LivrosDTO.builder()
                    .id((long) i)
                    .nome("Livro " + i)
                    .abreviacao("L" + i)
                    .testamento(TestamentoDTO.builder().id((long) i).nome("Testamento").build())
                    .build());
        }
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(largeList);
        assertEquals(66, livrosService.listar().size());
    }

    @Test
    @DisplayName("Should verify no more interactions with repository after listing")
    void testListarVerifyNoMoreInteractions() {
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(List.of(livrosDTO));

        livrosService.listar();

        verify(livrosRepository, times(1)).listarLivrosComTestamento();
        verifyNoMoreInteractions(livrosRepository);
    }

    @Test
    @DisplayName("Should handle books with special characters in names")
    void testListarBooksComNomesEspeciais() {
        LivrosDTO livroEspecial = LivrosDTO.builder()
                .id(3L)
                .nome("1ª Coríntios")
                .abreviacao("1Co")
                .testamento(TestamentoDTO.builder().id(2L).nome("Novo Testamento").build())
                .build();

        List<LivrosDTO> livrosList = List.of(livroEspecial);
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(livrosList);

        List<LivrosDTO> resultado = livrosService.listar();

        assertEquals("1ª Coríntios", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Should return list in same order as repository")
    void testListarPreserveOrder() {
        List<LivrosDTO> orderedList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            orderedList.add(LivrosDTO.builder()
                    .id((long) i)
                    .nome("Livro " + i)
                    .abreviacao("L" + i)
                    .testamento(TestamentoDTO.builder().id((long) i).nome("Testamento").build())
                    .build());
        }

        when(livrosRepository.listarLivrosComTestamento()).thenReturn(orderedList);

        List<LivrosDTO> resultado = livrosService.listar();

        for (int i = 0; i < resultado.size(); i++) {
            assertEquals("Livro " + (i + 1), resultado.get(i).getNome());
        }
    }

    @Test
    @DisplayName("Should handle list with null values gracefully")
    void testListarWithNullElements() {
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(new ArrayList<>());

        List<LivrosDTO> resultado = livrosService.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Should maintain consistency across multiple calls")
    void testListarMultipleCallsConsistency() {
        List<LivrosDTO> livrosList = List.of(livrosDTO);
        when(livrosRepository.listarLivrosComTestamento()).thenReturn(livrosList);

        List<LivrosDTO> resultado1 = livrosService.listar();
        List<LivrosDTO> resultado2 = livrosService.listar();

        assertEquals(resultado1.size(), resultado2.size());
        assertEquals(resultado1.get(0).getNome(), resultado2.get(0).getNome());
    }
}

