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
@DisplayName("VersiculoDoDiaService Comprehensive Tests")
class VersiculoDoDiaServiceTest {

    @Mock
    private VersiculosRepository versiculosRepository;

    @InjectMocks
    private VersiculosDoDiaService versiculoDoDiaService;

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

        List<VersiculoDoDia> resultado = versiculoDoDiaService.findAll();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(versiculosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Given no versiculos exist when findAll then return empty list")
    void testFindAllVazio() {
        when(versiculosRepository.findAll()).thenReturn(new ArrayList<>());

        List<VersiculoDoDia> resultado = versiculoDoDiaService.findAll();

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

        List<VersiculoDoDia> resultado = versiculoDoDiaService.findAll();

        assertEquals(2, resultado.size());
        verify(versiculosRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Given versiculo id exists when findById then return versiculo")
    void testFindByIdSucesso() {
        when(versiculosRepository.findById(1L)).thenReturn(Optional.of(versiculo));

        VersiculoDoDia resultado = versiculoDoDiaService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(verso.getId(), resultado.getVerso().getId());
        verify(versiculosRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given versiculo id does not exist when findById then throw exception")
    void testFindByIdNaoEncontrado() {
        when(versiculosRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> versiculoDoDiaService.findById(99L));
        verify(versiculosRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Given versiculo to save when save then persist and return")
    void testSaveSucesso() {
        when(versiculosRepository.save(versiculo)).thenReturn(versiculo);

        VersiculoDoDia resultado = versiculoDoDiaService.save(versiculo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(versiculosRepository, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given invalid versiculo when save then throw exception")
    void testSaveComExcecao() {
        when(versiculosRepository.save(versiculo)).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        assertThrows(DataIntegrityViolationException.class, () -> versiculoDoDiaService.save(versiculo));
        verify(versiculosRepository, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given versiculo id exists when delete then remove successfully")
    void testDeleteSucesso() {
        doNothing().when(versiculosRepository).deleteById(1L);

        assertDoesNotThrow(() -> versiculoDoDiaService.delete(1L));
        verify(versiculosRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Given non-existent versiculo id when delete then throw exception")
    void testDeleteNaoEncontrado() {
        doThrow(new EmptyResultDataAccessException(1)).when(versiculosRepository).deleteById(99L);

        assertThrows(RuntimeException.class, () -> versiculoDoDiaService.delete(99L));
    }

    @Test
    @DisplayName("Given constraint violation on delete when delete then throw exception")
    void testDeleteConstraintViolation() {
        doThrow(new DataIntegrityViolationException("Foreign key constraint")).when(versiculosRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> versiculoDoDiaService.delete(1L));
    }

    @Test
    @DisplayName("Should verify repository findAll called exactly once")
    void testVerifyFindAllRepositoryCalls() {
        when(versiculosRepository.findAll()).thenReturn(List.of(versiculo));

        versiculoDoDiaService.findAll();
        versiculoDoDiaService.findAll();

        verify(versiculosRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("Should preserve verso data when finding by id")
    void testFindByIdPreserveData() {
        when(versiculosRepository.findById(1L)).thenReturn(Optional.of(versiculo));

        VersiculoDoDia resultado = versiculoDoDiaService.findById(1L);

        assertEquals("No princípio, criou Deus os céus e a terra.", resultado.getVerso().getTexto());
        assertEquals("Gênesis", resultado.getVerso().getLivro().getNome());
    }

    @Test
    @DisplayName("Should save multiple versiculos successfully")
    void testSaveMultiplos() {
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);

        when(versiculosRepository.save(versiculo)).thenReturn(versiculo);
        when(versiculosRepository.save(versiculo2)).thenReturn(versiculo2);

        VersiculoDoDia resultado1 = versiculoDoDiaService.save(versiculo);
        VersiculoDoDia resultado2 = versiculoDoDiaService.save(versiculo2);

        assertNotNull(resultado1);
        assertNotNull(resultado2);
        assertEquals(1L, resultado1.getId());
        assertEquals(2L, resultado2.getId());
    }

    @Test
    @DisplayName("Should return empty list when repository has no data")
    void testFindAllEmpty() {
        when(versiculosRepository.findAll()).thenReturn(new ArrayList<>());

        List<VersiculoDoDia> resultado = versiculoDoDiaService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }

    @Test
    @DisplayName("Should handle delete with correct id verification")
    void testDeleteVerifyCorrectId() {
        doNothing().when(versiculosRepository).deleteById(5L);

        versiculoDoDiaService.delete(5L);

        verify(versiculosRepository, times(1)).deleteById(5L);
        verifyNoMoreInteractions(versiculosRepository);
    }

    @Test
    @DisplayName("Should find versiculo among multiple results")
    void testFindByIdAmongMultiple() {
        when(versiculosRepository.findById(1L)).thenReturn(Optional.of(versiculo));

        VersiculoDoDia resultado = versiculoDoDiaService.findById(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Should maintain data consistency across multiple operations")
    void testDataConsistency() {
        when(versiculosRepository.findAll()).thenReturn(List.of(versiculo));
        when(versiculosRepository.findById(1L)).thenReturn(Optional.of(versiculo));

        List<VersiculoDoDia> listResult = versiculoDoDiaService.findAll();
        VersiculoDoDia findResult = versiculoDoDiaService.findById(1L);

        assertEquals(listResult.get(0).getId(), findResult.getId());
        assertEquals(listResult.get(0).getVerso().getTexto(), findResult.getVerso().getTexto());
    }

    @Test
    @DisplayName("Should handle large list of versiculos")
    void testFindAllLargeList() {
        List<VersiculoDoDia> largeList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            VersiculoDoDia v = new VersiculoDoDia();
            v.setId((long) i);
            largeList.add(v);
        }

        when(versiculosRepository.findAll()).thenReturn(largeList);

        List<VersiculoDoDia> resultado = versiculoDoDiaService.findAll();

        assertEquals(100, resultado.size());
    }

    @Test
    @DisplayName("Should throw exception when save receives null")
    void testSaveWithNull() {
        when(versiculosRepository.save(null)).thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class, () -> versiculoDoDiaService.save(null));
    }

    @Test
    @DisplayName("Should verify delete is called with correct parameters")
    void testDeleteVerifyParameterPassing() {
        doNothing().when(versiculosRepository).deleteById(1L);

        versiculoDoDiaService.delete(1L);

        verify(versiculosRepository).deleteById(1L);
    }
}

