package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.dtos.TestamentoDTO;
import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.entities.biblia.Testamento;
import com.valdirsantos714.biblia.entities.biblia.Versao;
import com.valdirsantos714.biblia.repositories.VersiculosRepository;
import com.valdirsantos714.biblia.repositories.VersosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
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

    @Mock
    private VersosRepository versosRepository;

    @InjectMocks
    private VersiculosDoDiaService versiculoDoDiaService;

    private VersiculoDoDia versiculo;
    private Versos verso;
    private Livros livro;
    private Testamento testamento;
    private Versao versao;
    private VersosDTO versosDTO;

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

        TestamentoDTO testamentoDTO = new TestamentoDTO(1L, "Antigo Testamento");
        LivrosDTO livrosDTO = new LivrosDTO(1L, "Gênesis", "Gn", testamentoDTO);
        VersaoDTO versaoDTO = new VersaoDTO(1L, "NVT");

        versosDTO = new VersosDTO(1L, versaoDTO, livrosDTO, 1, 1, "No princípio, criou Deus os céus e a terra.", 1);
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
    @DisplayName("Given verso aleatorio exists when findVersoAleatorioDoDia then return VersosDTO")
    void testFindVersoAleatorioDoDiaSucesso() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(data);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("No princípio, criou Deus os céus e a terra.", resultado.getTexto());
        verify(versosRepository, times(1)).findVersoAleatorioDoDia(data);
    }

    @Test
    @DisplayName("Given verso aleatorio not found when findVersoAleatorioDoDia then throw exception")
    void testFindVersoAleatorioDoDiaNaoEncontrado() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> versiculoDoDiaService.findVersoAleatorioDoDia(data));
        verify(versosRepository, times(1)).findVersoAleatorioDoDia(data);
    }

    @Test
    @DisplayName("Given verso aleatorio exists when findVersoAleatorioDoDia then verify converted DTO")
    void testFindVersoAleatorioDoDiaVerifyDTO() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(data);

        assertNotNull(resultado.getVersao());
        assertNotNull(resultado.getLivro());
        assertEquals("Gênesis", resultado.getLivro().getNome());
        assertEquals("Gn", resultado.getLivro().getAbreviacao());
        assertEquals(1, resultado.getCapitulo());
        assertEquals(1, resultado.getVersiculo());
    }

    @Test
    @DisplayName("Given verso with testamento when findVersoAleatorioDoDia then include testamento in DTO")
    void testFindVersoAleatorioDoDiaComTestamento() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(data);

        assertEquals(1, resultado.getTestamento());
        assertNotNull(resultado.getLivro().getTestamento());
        assertEquals("Antigo Testamento", resultado.getLivro().getTestamento().getNome());
    }

    @Test
    @DisplayName("Given verso with different data when findVersoAleatorioDoDia then call repository with correct date")
    void testFindVersoAleatorioDoDiaComDataDiferente() {
        LocalDate dataCustom = LocalDate.of(2025, 12, 25);
        when(versosRepository.findVersoAleatorioDoDia(dataCustom)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(dataCustom);

        assertNotNull(resultado);
        verify(versosRepository, times(1)).findVersoAleatorioDoDia(dataCustom);
    }

    @Test
    @DisplayName("Given multiple calls to findVersoAleatorioDoDia then verify repository called each time")
    void testFindVersoAleatorioDoDiaMultiplasChamadas() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        versiculoDoDiaService.findVersoAleatorioDoDia(data);
        versiculoDoDiaService.findVersoAleatorioDoDia(data);
        versiculoDoDiaService.findVersoAleatorioDoDia(data);

        verify(versosRepository, times(3)).findVersoAleatorioDoDia(data);
    }

    @Test
    @DisplayName("Given verso to save when save then persist and return")
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
    @DisplayName("Should find verso aleatorio with correct conversion to DTO")
    void testFindVersoAleatorioDoDiaConversion() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(data);

        assertEquals(verso.getId(), resultado.getId());
        assertEquals(verso.getCapitulo(), resultado.getCapitulo());
        assertEquals(verso.getVersiculo(), resultado.getVersiculo());
        assertEquals(verso.getTexto(), resultado.getTexto());
    }

    @Test
    @DisplayName("Should find verso aleatorio with livro and versao data preserved")
    void testFindVersoAleatorioDoDiaPreserveLivroVersao() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(data);

        assertEquals(verso.getLivro().getId(), resultado.getLivro().getId());
        assertEquals(verso.getLivro().getNome(), resultado.getLivro().getNome());
        assertEquals(verso.getVersao().getId(), resultado.getVersao().getId());
        assertEquals(verso.getVersao().getNome(), resultado.getVersao().getNome());
    }

    @Test
    @DisplayName("Should handle verso with complete testamento hierarchy")
    void testFindVersoAleatorioDoDiaTestamentoHierarchy() {
        LocalDate data = LocalDate.now();
        when(versosRepository.findVersoAleatorioDoDia(data)).thenReturn(verso);

        VersosDTO resultado = versiculoDoDiaService.findVersoAleatorioDoDia(data);

        assertNotNull(resultado.getLivro().getTestamento());
        assertEquals(verso.getLivro().getTestamento().getId(), resultado.getLivro().getTestamento().getId());
        assertEquals(verso.getLivro().getTestamento().getNome(), resultado.getLivro().getTestamento().getNome());
    }

    @Test
    @DisplayName("Should verify delete is called with correct parameters")
    void testDeleteVerifyParameterPassing() {
        doNothing().when(versiculosRepository).deleteById(1L);

        versiculoDoDiaService.delete(1L);

        verify(versiculosRepository).deleteById(1L);
    }
}