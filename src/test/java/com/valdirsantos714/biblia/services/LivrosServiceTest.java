
package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.entities.biblia.Testamento;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LivrosService Tests")
class LivrosServiceTest {

    @Mock
    private LivrosRepository livrosRepository;

    @InjectMocks
    private LivrosService livrosService;

    private Livros livro;
    private Testamento testamento;

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
    }

    @Test
    @DisplayName("Should list all books successfully")
    void testListarLivros() {
        List<Livros> livrosList = new ArrayList<>();
        livrosList.add(livro);

        when(livrosRepository.findAll()).thenReturn(livrosList);

        List<Livros> resultado = livrosService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Gênesis", resultado.get(0).getNome());
        verify(livrosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no books found")
    void testListarLivrosVazio() {
        when(livrosRepository.findAll()).thenReturn(new ArrayList<>());

        List<Livros> resultado = livrosService.listar();

        assertTrue(resultado.isEmpty());
        verify(livrosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find book by id successfully")
    void testFindByIdSucesso() {
        when(livrosRepository.findById(1L)).thenReturn(Optional.of(livro));

        Livros resultado = livrosService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Gênesis", resultado.getNome());
        verify(livrosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when book not found by id")
    void testFindByIdNaoEncontrado() {
        when(livrosRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> livrosService.findById(99L));
        verify(livrosRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should find book by name successfully")
    void testPegaLivroPorNomeSucesso() {
        List<Livros> livrosList = new ArrayList<>();
        livrosList.add(livro);

        when(livrosRepository.findAll()).thenReturn(livrosList);

        Livros resultado = livrosService.pegaLivroPorNome("Gênesis");

        assertNotNull(resultado);
        assertEquals("Gênesis", resultado.getNome());
        verify(livrosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when book not found by name")
    void testPegaLivroPorNomeNaoEncontrado() {
        when(livrosRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(RuntimeException.class, () -> livrosService.pegaLivroPorNome("Inexistente"));
        verify(livrosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle multiple books in list")
    void testListarMultiplosLivros() {
        Livros livro2 = new Livros();
        livro2.setId(2L);
        livro2.setNome("Êxodo");
        livro2.setAbreviacao("Ex");
        livro2.setTestamento(testamento);

        List<Livros> livrosList = new ArrayList<>();
        livrosList.add(livro);
        livrosList.add(livro2);

        when(livrosRepository.findAll()).thenReturn(livrosList);

        List<Livros> resultado = livrosService.listar();

        assertEquals(2, resultado.size());
        assertEquals("Gênesis", resultado.get(0).getNome());
        assertEquals("Êxodo", resultado.get(1).getNome());
        verify(livrosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find correct book among multiple books by name")
    void testPegaLivroPorNomeMultiplosLivros() {
        Livros livro2 = new Livros();
        livro2.setId(2L);
        livro2.setNome("Êxodo");
        livro2.setAbreviacao("Ex");
        livro2.setTestamento(testamento);

        List<Livros> livrosList = new ArrayList<>();
        livrosList.add(livro);
        livrosList.add(livro2);

        when(livrosRepository.findAll()).thenReturn(livrosList);

        Livros resultado = livrosService.pegaLivroPorNome("Êxodo");

        assertEquals("Êxodo", resultado.getNome());
        assertEquals(2L, resultado.getId());
        verify(livrosRepository, times(1)).findAll();
    }
}

