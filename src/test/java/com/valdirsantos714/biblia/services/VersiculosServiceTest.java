package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.entities.biblia.Testamento;
import com.valdirsantos714.biblia.entities.biblia.Versao;
import com.valdirsantos714.biblia.repositories.VersiculosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VersiculosService Comprehensive Tests")
class VersiculosServiceTest {

    @Mock
    private VersiculosRepository versiculosRepository;

    @InjectMocks
    private VersiculosService versiculosService;

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
    @DisplayName("Given verso exists when findAll then return list with versiculo")
    void testFindAllComVersiculo() {
        List<VersiculoDoDia> versiculos = new ArrayList<>();
        versiculos.add(versiculo);

        when(versiculosRepository.findAll()).thenReturn(versiculos);

        List<VersiculoDoDia> resultado = versiculosService.findAll();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(versiculosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Given no versiculos exist when findAll then return empty list")
    void testFindAllVazio() {
        when(versiculosRepository.findAll()).thenReturn(new ArrayList<>());

        List<VersiculoDoDia> resultado = versiculosService.findAll();

        assertTrue(resultado.isEmpty());
        verify(versiculosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Given multiple versiculos exist when findAll then return all")
    void testFindAllMultiplos() {
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);
        Versos verso2 = new Versos();
        verso2.setId(2L);
        verso2.setCapitulo(1);
        verso2.setVersiculo(2);
        verso2.setTexto("E a terra era sem forma e vazia.");
        versiculo2.setVerso(verso2);

        List<VersiculoDoDia> versiculos = new ArrayList<>();
        versiculos.add(versiculo);
        versiculos.add(versiculo2);

        when(versiculosRepository.findAll()).thenReturn(versiculos);

        List<VersiculoDoDia> resultado = versiculosService.findAll();

        assertEquals(2, resultado.size());
        verify(versiculosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Given versiculo id exists when findById then return versiculo")
    void testFindByIdSucesso() {
        when(versiculosRepository.findById(1L)).thenReturn(Optional.of(versiculo));

        VersiculoDoDia resultado = versiculosService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(verso.getId(), resultado.getVerso().getId());
        verify(versiculosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given versiculo id does not exist when findById then throw exception")
    void testFindByIdNaoEncontrado() {
        when(versiculosRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> versiculosService.findById(99L));
        verify(versiculosRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Given versiculo to save when save then persist and return")
    void testSaveSucesso() {
        when(versiculosRepository.save(versiculo)).thenReturn(versiculo);

        VersiculoDoDia resultado = versiculosService.save(versiculo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(versiculosRepository, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given versiculo with verso when save then persist with verso")
    void testSaveComVerso() {
        when(versiculosRepository.save(versiculo)).thenReturn(versiculo);

        VersiculoDoDia resultado = versiculosService.save(versiculo);

        assertNotNull(resultado.getVerso());
        assertEquals(verso.getId(), resultado.getVerso().getId());
        assertEquals("No princípio, criou Deus os céus e a terra.", resultado.getVerso().getTexto());
        verify(versiculosRepository, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given versiculo id when delete then remove successfully")
    void testDeleteSucesso() {
        doNothing().when(versiculosRepository).deleteById(1L);

        versiculosService.delete(1L);

        verify(versiculosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Given invalid versiculo id when delete then throw exception")
    void testDeleteNaoEncontrado() {
        doThrow(new EmptyResultDataAccessException(1)).when(versiculosRepository).deleteById(99L);

        assertThrows(RuntimeException.class, () -> versiculosService.delete(99L));
        verify(versiculosRepository, times(1)).deleteById(99L);
    }

    @Test
    @DisplayName("Given constraint violation when delete then throw exception")
    void testDeleteComViolacaoConstraint() {
        doThrow(new DataIntegrityViolationException("Violation")).when(versiculosRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> versiculosService.delete(1L));
        verify(versiculosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Given new versiculo when save then generate id")
    void testSaveNovoVersiculo() {
        VersiculoDoDia novoVersiculo = new VersiculoDoDia();
        novoVersiculo.setVerso(verso);

        when(versiculosRepository.save(novoVersiculo)).thenReturn(versiculo);

        VersiculoDoDia resultado = versiculosService.save(novoVersiculo);

        assertNotNull(resultado.getId());
        assertEquals(1L, resultado.getId());
        verify(versiculosRepository, times(1)).save(novoVersiculo);
    }

    @Test
    @DisplayName("Given versiculo with different verso when save then update verso")
    void testSaveAtualizaVerso() {
        Versos versoAtualizado = new Versos();
        versoAtualizado.setId(2L);
        versoAtualizado.setCapitulo(2);
        versoAtualizado.setVersiculo(1);
        versoAtualizado.setTexto("Texto atualizado");

        VersiculoDoDia versiculoAtualizado = new VersiculoDoDia();
        versiculoAtualizado.setId(1L);
        versiculoAtualizado.setVerso(versoAtualizado);

        when(versiculosRepository.save(versiculoAtualizado)).thenReturn(versiculoAtualizado);

        VersiculoDoDia resultado = versiculosService.save(versiculoAtualizado);

        assertEquals(2L, resultado.getVerso().getId());
        assertEquals("Texto atualizado", resultado.getVerso().getTexto());
        verify(versiculosRepository, times(1)).save(versiculoAtualizado);
    }

    @Test
    @DisplayName("Given versiculo id when findById then call repository once")
    void testFindByIdVerificaRepositorioUmavez() {
        when(versiculosRepository.findById(1L)).thenReturn(Optional.of(versiculo));

        versiculosService.findById(1L);

        verify(versiculosRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(versiculosRepository);
    }

    @Test
    @DisplayName("Given multiple delete operations when delete then execute sequentially")
    void testMultiploDeleteSucesso() {
        doNothing().when(versiculosRepository).deleteById(anyLong());

        versiculosService.delete(1L);
        versiculosService.delete(2L);
        versiculosService.delete(3L);

        verify(versiculosRepository, times(3)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Given versiculo with full data when save then preserve all fields")
    void testSavePreservaTodsOsCampos() {
        when(versiculosRepository.save(versiculo)).thenReturn(versiculo);

        VersiculoDoDia resultado = versiculosService.save(versiculo);

        assertEquals(versiculo.getId(), resultado.getId());
        assertEquals(versiculo.getVerso().getId(), resultado.getVerso().getId());
        assertEquals(versiculo.getVerso().getCapitulo(), resultado.getVerso().getCapitulo());
        assertEquals(versiculo.getVerso().getVersiculo(), resultado.getVerso().getVersiculo());
        assertEquals(versiculo.getVerso().getTexto(), resultado.getVerso().getTexto());
        verify(versiculosRepository, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given versiculo from different livro when findById then return correctly")
    void testFindByIdDiferenteLivro() {
        Livros outroLivro = new Livros();
        outroLivro.setId(2L);
        outroLivro.setNome("Êxodo");

        Versos versoOutroLivro = new Versos();
        versoOutroLivro.setId(2L);
        versoOutroLivro.setLivro(outroLivro);

        VersiculoDoDia versiculoOutroLivro = new VersiculoDoDia();
        versiculoOutroLivro.setId(2L);
        versiculoOutroLivro.setVerso(versoOutroLivro);

        when(versiculosRepository.findById(2L)).thenReturn(Optional.of(versiculoOutroLivro));

        VersiculoDoDia resultado = versiculosService.findById(2L);

        assertEquals(2L, resultado.getVerso().getLivro().getId());
        assertEquals("Êxodo", resultado.getVerso().getLivro().getNome());
        verify(versiculosRepository, times(1)).findById(2L);
    }
}

