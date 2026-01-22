package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.dtos.TestamentoDTO;
import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.entities.biblia.Testamento;
import com.valdirsantos714.biblia.entities.biblia.Versao;
import com.valdirsantos714.biblia.services.VersiculosDoDiaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VersiculoDoDiaController Tests")
class VersiculoDoDiaControllerTest {

    @Mock
    private VersiculosDoDiaService versiculoDoDiaService;

    @InjectMocks
    private VersiculoDoDiaController versiculoDoDiaController;

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

        TestamentoDTO testamentoDTO = new TestamentoDTO();
        testamentoDTO.setId(1L);
        testamentoDTO.setNome("Antigo Testamento");

        LivrosDTO livrosDTO = new LivrosDTO();
        livrosDTO.setId(1L);
        livrosDTO.setNome("Gênesis");
        livrosDTO.setAbreviacao("Gn");
        livrosDTO.setTestamento(testamentoDTO);

        VersaoDTO versaoDTO = new VersaoDTO();
        versaoDTO.setId(1L);
        versaoDTO.setNome("NVT");

        versosDTO = new VersosDTO();
        versosDTO.setId(1L);
        versosDTO.setCapitulo(1);
        versosDTO.setVersiculo(1);
        versosDTO.setTexto("No princípio, criou Deus os céus e a terra.");
        versosDTO.setTestamento(1);
        versosDTO.setLivro(livrosDTO);
        versosDTO.setVersao(versaoDTO);
    }

    @Test
    @DisplayName("Given versiculos exist when findAllVersiculoDoDias then return 200 with list")
    void testFindAllVersiculoDoDias() {
        List<VersiculoDoDia> versiculos = new ArrayList<>();
        versiculos.add(versiculo);

        when(versiculoDoDiaService.findAll()).thenReturn(versiculos);

        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        verify(versiculoDoDiaService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given no versiculos exist when findAllVersiculoDoDias then return 200 with empty list")
    void testFindAllVersiculoDoDiasEmpty() {
        when(versiculoDoDiaService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(versiculoDoDiaService, times(1)).findAll();
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
        when(versiculoDoDiaService.findAll()).thenReturn(versiculos);
        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(versiculoDoDiaService, times(1)).findAll();
    }

    @Test
    @DisplayName("Given versiculo id exists when findByIdVersiculoDoDia then return 200 with versiculo")
    void testFindByIdVersiculoDoDia() {
        when(versiculoDoDiaService.findById(1L)).thenReturn(versiculo);
        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.findByIdVersiculoDoDia(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(versiculoDoDiaService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Given versiculo id does not exist when findByIdVersiculoDoDia then throw exception")
    void testFindByIdVersiculoDoDiaNotFound() {
        when(versiculoDoDiaService.findById(99L)).thenThrow(new RuntimeException("Erro"));
        assertThrows(RuntimeException.class, () -> versiculoDoDiaController.findByIdVersiculoDoDia(99L));
        verify(versiculoDoDiaService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Given verso aleatorio exists when getVersiculoDoDia then return 200 with verso")
    void testGetVersiculoDoDia() {
        when(versiculoDoDiaService.findVersoAleatorioDoDia(any(LocalDate.class))).thenReturn(versosDTO);
        ResponseEntity<VersosDTO> response = versiculoDoDiaController.getVersiculoDoDia();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("No princípio, criou Deus os céus e a terra.", response.getBody().getTexto());
        verify(versiculoDoDiaService, times(1)).findVersoAleatorioDoDia(any(LocalDate.class));
    }

    @Test
    @DisplayName("Given verso aleatorio not found when getVersiculoDoDia then throw exception")
    void testGetVersiculoDoDiaNotFound() {
        when(versiculoDoDiaService.findVersoAleatorioDoDia(any(LocalDate.class))).thenThrow(new RuntimeException("Nenhum versículo encontrado"));
        assertThrows(RuntimeException.class, () -> versiculoDoDiaController.getVersiculoDoDia());
        verify(versiculoDoDiaService, times(1)).findVersoAleatorioDoDia(any(LocalDate.class));
    }

    @Test
    @DisplayName("Given verso with correct data when getVersiculoDoDia then verify all fields")
    void testGetVersiculoDoDiaVerifyFields() {
        when(versiculoDoDiaService.findVersoAleatorioDoDia(any(LocalDate.class))).thenReturn(versosDTO);
        ResponseEntity<VersosDTO> response = versiculoDoDiaController.getVersiculoDoDia();
        VersosDTO verso = response.getBody();
        assertNotNull(verso.getId());
        assertNotNull(verso.getVersao());
        assertNotNull(verso.getLivro());
        assertEquals("NVT", verso.getVersao().getNome());
        assertEquals("Gênesis", verso.getLivro().getNome());
        assertEquals(1, verso.getCapitulo());
        assertEquals(1, verso.getVersiculo());
    }

    @Test
    @DisplayName("Given verso aleatorio when getVersiculoDoDia then response contains complete data")
    void testGetVersiculoDoDiaCompleteData() {
        when(versiculoDoDiaService.findVersoAleatorioDoDia(any(LocalDate.class))).thenReturn(versosDTO);
        ResponseEntity<VersosDTO> response = versiculoDoDiaController.getVersiculoDoDia();
        assertTrue(response.getBody().getTexto().length() > 0);
        assertNotNull(response.getBody().getLivro().getTestamento());
        assertEquals(1, response.getBody().getTestamento());
    }

    @Test
    @DisplayName("Given verso to save when saveVerso then return 201 with created verso")
    void testSaveVerso() {
        when(versiculoDoDiaService.save(versiculo)).thenReturn(versiculo);
        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(versiculo);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(versiculoDoDiaService, times(1)).save(versiculo);
    }

    @Test
    @DisplayName("Given verso is valid when saveVerso then save successfully")
    void testSaveVersoValid() {
        VersiculoDoDia newVersiculo = new VersiculoDoDia();
        newVersiculo.setId(2L);
        when(versiculoDoDiaService.save(newVersiculo)).thenReturn(newVersiculo);
        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(newVersiculo);
        assertEquals(2L, response.getBody().getId());
        verify(versiculoDoDiaService, times(1)).save(newVersiculo);
    }

    @Test
    @DisplayName("Should verify response contains correct status code")
    void testResponseStatusCode() {
        when(versiculoDoDiaService.findAll()).thenReturn(List.of(versiculo));
        ResponseEntity<List<VersiculoDoDia>> response1 = versiculoDoDiaController.findAllVersiculoDoDias();
        ResponseEntity<List<VersiculoDoDia>> response2 = versiculoDoDiaController.findAllVersiculoDoDias();
        assertEquals(response1.getStatusCode(), response2.getStatusCode());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
    }

    @Test
    @DisplayName("Should verify service method called correct number of times")
    void testVerifyServiceCallCount() {
        when(versiculoDoDiaService.findAll()).thenReturn(List.of(versiculo));
        versiculoDoDiaController.findAllVersiculoDoDias();
        versiculoDoDiaController.findAllVersiculoDoDias();
        versiculoDoDiaController.findAllVersiculoDoDias();
        verify(versiculoDoDiaService, times(3)).findAll();
    }

    @Test
    @DisplayName("Should handle save with correct parameter passing")
    void testSaveParameterPassing() {
        VersiculoDoDia newVersiculo = new VersiculoDoDia();
        newVersiculo.setId(2L);
        when(versiculoDoDiaService.save(newVersiculo)).thenReturn(newVersiculo);
        versiculoDoDiaController.saveVerso(newVersiculo);
        verify(versiculoDoDiaService, times(1)).save(newVersiculo);
    }

    @Test
    @DisplayName("Should preserve list order from service")
    void testPreserveListOrder() {
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);
        VersiculoDoDia versiculo3 = new VersiculoDoDia();
        versiculo3.setId(3L);
        List<VersiculoDoDia> versiculos = List.of(versiculo, versiculo2, versiculo3);
        when(versiculoDoDiaService.findAll()).thenReturn(versiculos);
        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals(2L, response.getBody().get(1).getId());
        assertEquals(3L, response.getBody().get(2).getId());
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
        when(versiculoDoDiaService.findAll()).thenReturn(largeList);
        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();
        assertEquals(100, response.getBody().size());
    }

    @Test
    @DisplayName("Should verify response body is not null")
    void testResponseBodyNotNull() {
        when(versiculoDoDiaService.findAll()).thenReturn(List.of(versiculo));
        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should handle multiple find operations")
    void testMultipleFindOperations() {
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);
        when(versiculoDoDiaService.findById(1L)).thenReturn(versiculo);
        when(versiculoDoDiaService.findById(2L)).thenReturn(versiculo2);
        ResponseEntity<VersiculoDoDia> response1 = versiculoDoDiaController.findByIdVersiculoDoDia(1L);
        ResponseEntity<VersiculoDoDia> response2 = versiculoDoDiaController.findByIdVersiculoDoDia(2L);
        assertEquals(1L, response1.getBody().getId());
        assertEquals(2L, response2.getBody().getId());
    }

    @Test
    @DisplayName("Should handle multiple save operations")
    void testMultipleSaveOperations() {
        VersiculoDoDia versiculo1 = new VersiculoDoDia();
        versiculo1.setId(1L);
        VersiculoDoDia versiculo2 = new VersiculoDoDia();
        versiculo2.setId(2L);
        when(versiculoDoDiaService.save(versiculo1)).thenReturn(versiculo1);
        when(versiculoDoDiaService.save(versiculo2)).thenReturn(versiculo2);
        ResponseEntity<VersiculoDoDia> response1 = versiculoDoDiaController.saveVerso(versiculo1);
        ResponseEntity<VersiculoDoDia> response2 = versiculoDoDiaController.saveVerso(versiculo2);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        assertEquals(1L, response1.getBody().getId());
        assertEquals(2L, response2.getBody().getId());
    }

    @Test
    @DisplayName("Should verify service not called when exception occurs")
    void testExceptionHandling() {
        when(versiculoDoDiaService.findById(99L)).thenThrow(new RuntimeException("Not found"));
        try {
            versiculoDoDiaController.findByIdVersiculoDoDia(99L);
        } catch (RuntimeException e) {
            assertEquals("Not found", e.getMessage());
        }
        verify(versiculoDoDiaService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should verify findAll returns correct list type")
    void testFindAllReturnsListType() {
        when(versiculoDoDiaService.findAll()).thenReturn(List.of(versiculo));
        ResponseEntity<List<VersiculoDoDia>> response = versiculoDoDiaController.findAllVersiculoDoDias();
        assertTrue(response.getBody() instanceof List);
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should preserve verso relationships in response")
    void testPreserveVersoRelationships() {
        when(versiculoDoDiaService.findById(1L)).thenReturn(versiculo);
        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.findByIdVersiculoDoDia(1L);
        VersiculoDoDia result = response.getBody();
        assertNotNull(result.getVerso());
        assertNotNull(result.getVerso().getLivro());
        assertNotNull(result.getVerso().getVersao());
        assertEquals("Gênesis", result.getVerso().getLivro().getNome());
    }

    @Test
    @DisplayName("Should verify save maintains id consistency")
    void testSaveMaintainsIdConsistency() {
        VersiculoDoDia versiculo5 = new VersiculoDoDia();
        versiculo5.setId(5L);
        when(versiculoDoDiaService.save(versiculo5)).thenReturn(versiculo5);
        ResponseEntity<VersiculoDoDia> response = versiculoDoDiaController.saveVerso(versiculo5);

        assertEquals(5L, response.getBody().getId());
    }
}
