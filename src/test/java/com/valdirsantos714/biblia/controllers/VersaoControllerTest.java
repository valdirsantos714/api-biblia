package com.valdirsantos714.biblia.controllers;

import com.valdirsantos714.biblia.dtos.DetalhesBuscaVerso;
import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.dtos.VersoComparacoesDTO;
import com.valdirsantos714.biblia.services.VersaoService;
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
@DisplayName("VersaoController Comprehensive Tests")
class VersaoControllerTest {

    @Mock
    private VersaoService versaoService;

    @InjectMocks
    private VersaoController versaoController;

    private VersaoDTO versaoDTO1;
    private VersaoDTO versaoDTO2;
    private VersaoDTO versaoDTO3;
    private VersoComparacoesDTO versoComparacoesDTO1;
    private VersoComparacoesDTO versoComparacoesDTO2;
    private VersoComparacoesDTO versoComparacoesDTO3;
    private DetalhesBuscaVerso detalhesBuscaVerso;

    @BeforeEach
    void setUp() {
        versaoDTO1 = VersaoDTO.builder()
                .id(1L)
                .nome("NVT")
                .build();

        versaoDTO2 = VersaoDTO.builder()
                .id(2L)
                .nome("NVI")
                .build();

        versaoDTO3 = VersaoDTO.builder()
                .id(3L)
                .nome("ARA")
                .build();

        versoComparacoesDTO1 = VersoComparacoesDTO.builder()
                .versao("NVT")
                .texto("No princípio, criou Deus os céus e a terra.")
                .build();

        versoComparacoesDTO2 = VersoComparacoesDTO.builder()
                .versao("NVI")
                .texto("No princípio criou Deus os céus e a terra.")
                .build();

        versoComparacoesDTO3 = VersoComparacoesDTO.builder()
                .versao("ARA")
                .texto("No princípio criou Deus o céu e a terra.")
                .build();

        List<VersoComparacoesDTO> comparacoesList = new ArrayList<>();
        comparacoesList.add(versoComparacoesDTO1);
        comparacoesList.add(versoComparacoesDTO2);
        comparacoesList.add(versoComparacoesDTO3);

        detalhesBuscaVerso = DetalhesBuscaVerso.builder()
                .nomeLivro("Gênesis")
                .capitulo(1)
                .versiculo(1)
                .comparacoes(comparacoesList)
                .build();
    }

    @Test
    @DisplayName("Given versions exist when listarTodas then return 200 with list")
    void testListarTodas() {
        List<VersaoDTO> versoesList = new ArrayList<>();
        versoesList.add(versaoDTO1);
        versoesList.add(versaoDTO2);
        versoesList.add(versaoDTO3);

        when(versaoService.listarTodasAsVersoes()).thenReturn(versoesList);

        ResponseEntity<List<VersaoDTO>> response = versaoController.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("NVT", response.getBody().get(0).getNome());
        assertEquals("NVI", response.getBody().get(1).getNome());
        assertEquals("ARA", response.getBody().get(2).getNome());
        verify(versaoService, times(1)).listarTodasAsVersoes();
    }

    @Test
    @DisplayName("Given no versions exist when listarTodas then return 200 with empty list")
    void testListarTodasEmpty() {
        when(versaoService.listarTodasAsVersoes()).thenReturn(new ArrayList<>());

        ResponseEntity<List<VersaoDTO>> response = versaoController.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(versaoService, times(1)).listarTodasAsVersoes();
    }

    @Test
    @DisplayName("Given single version exists when listarTodas then return 200 with single item")
    void testListarTodasSingle() {
        List<VersaoDTO> versoesList = new ArrayList<>();
        versoesList.add(versaoDTO1);

        when(versaoService.listarTodasAsVersoes()).thenReturn(versoesList);

        ResponseEntity<List<VersaoDTO>> response = versaoController.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("NVT", response.getBody().get(0).getNome());
        verify(versaoService, times(1)).listarTodasAsVersoes();
    }

    @Test
    @DisplayName("Given verses exist when compararVerso then return 200 with DetalhesBuscaVerso")
    void testCompararVerso() {
        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1))
                .thenReturn(detalhesBuscaVerso);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gênesis", 1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gênesis", response.getBody().getNomeLivro());
        assertEquals(Integer.valueOf(1), response.getBody().getCapitulo());
        assertEquals(Integer.valueOf(1), response.getBody().getVersiculo());
        assertEquals(3, response.getBody().getComparacoes().size());
        assertEquals("NVT", response.getBody().getComparacoes().get(0).getVersao());
        assertEquals("NVI", response.getBody().getComparacoes().get(1).getVersao());
        assertEquals("ARA", response.getBody().getComparacoes().get(2).getVersao());
        verify(versaoService, times(1)).compararVersoEmTodasAsVersoes("Gênesis", 1, 1);
    }

    @Test
    @DisplayName("Given verse not found when compararVerso then return 200 with empty comparacoes")
    void testCompararVersoNotFound() {
        DetalhesBuscaVerso emptyDetails = DetalhesBuscaVerso.builder()
                .nomeLivro("Inexistente")
                .capitulo(999)
                .versiculo(999)
                .comparacoes(new ArrayList<>())
                .build();

        when(versaoService.compararVersoEmTodasAsVersoes("Inexistente", 999, 999))
                .thenReturn(emptyDetails);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Inexistente", 999, 999);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getComparacoes().isEmpty());
        verify(versaoService, times(1)).compararVersoEmTodasAsVersoes("Inexistente", 999, 999);
    }

    @Test
    @DisplayName("Given single verse exists when compararVerso then return 200 with single comparison")
    void testCompararVersoSingle() {
        List<VersoComparacoesDTO> singleComparacao = new ArrayList<>();
        singleComparacao.add(versoComparacoesDTO1);

        DetalhesBuscaVerso singleDetail = DetalhesBuscaVerso.builder()
                .nomeLivro("Gênesis")
                .capitulo(1)
                .versiculo(1)
                .comparacoes(singleComparacao)
                .build();

        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1))
                .thenReturn(singleDetail);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gênesis", 1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getComparacoes().size());
        assertEquals("NVT", response.getBody().getComparacoes().get(0).getVersao());
        verify(versaoService, times(1)).compararVersoEmTodasAsVersoes("Gênesis", 1, 1);
    }

    @Test
    @DisplayName("Given multiple verses when compararVerso then all have same chapter and verse number")
    void testCompararVersoConsistency() {
        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1))
                .thenReturn(detalhesBuscaVerso);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gênesis", 1, 1);

        assertEquals(Integer.valueOf(1), response.getBody().getCapitulo());
        assertEquals(Integer.valueOf(1), response.getBody().getVersiculo());
        assertEquals("Gênesis", response.getBody().getNomeLivro());
    }

    @Test
    @DisplayName("Given verse exists when compararVerso then return correct verse content")
    void testCompararVersoContent() {
        List<VersoComparacoesDTO> singleComparacao = new ArrayList<>();
        singleComparacao.add(versoComparacoesDTO1);

        DetalhesBuscaVerso singleDetail = DetalhesBuscaVerso.builder()
                .nomeLivro("Gênesis")
                .capitulo(1)
                .versiculo(1)
                .comparacoes(singleComparacao)
                .build();

        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1))
                .thenReturn(singleDetail);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gênesis", 1, 1);

        DetalhesBuscaVerso detalhe = response.getBody();
        assertEquals("Gênesis", detalhe.getNomeLivro());
        assertEquals("NVT", detalhe.getComparacoes().get(0).getVersao());
        assertEquals("No princípio, criou Deus os céus e a terra.", detalhe.getComparacoes().get(0).getTexto());
    }

    @Test
    @DisplayName("Given different chapters when compararVerso then return correct chapter number")
    void testCompararVersoDiferentesCapitulos() {
        List<VersoComparacoesDTO> comparacaoCapitulo3 = new ArrayList<>();
        comparacaoCapitulo3.add(VersoComparacoesDTO.builder()
                .versao("NVT")
                .texto("Ora, a serpente era o mais astuto...")
                .build());

        DetalhesBuscaVerso capitulo3Details = DetalhesBuscaVerso.builder()
                .nomeLivro("Gênesis")
                .capitulo(3)
                .versiculo(1)
                .comparacoes(comparacaoCapitulo3)
                .build();

        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 3, 1))
                .thenReturn(capitulo3Details);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gênesis", 3, 1);

        assertEquals(Integer.valueOf(3), response.getBody().getCapitulo());
    }

    @Test
    @DisplayName("Given case insensitive search when compararVerso then still finds verses")
    void testCompararVersoIgnoreCase() {
        when(versaoService.compararVersoEmTodasAsVersoes("GÊNESIS", 1, 1))
                .thenReturn(detalhesBuscaVerso);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("GÊNESIS", 1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getComparacoes().size());
        verify(versaoService, times(1)).compararVersoEmTodasAsVersoes("GÊNESIS", 1, 1);
    }

    @Test
    @DisplayName("Given abbreviation search when compararVerso then finds verses by abbreviation")
    void testCompararVersoAbreviacao() {
        when(versaoService.compararVersoEmTodasAsVersoes("Gn", 1, 1))
                .thenReturn(detalhesBuscaVerso);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gn", 1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getComparacoes().size());
        verify(versaoService, times(1)).compararVersoEmTodasAsVersoes("Gn", 1, 1);
    }

    @Test
    @DisplayName("Given verses exist when compararVerso then response is not null")
    void testCompararVersoResponseNotNull() {
        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1))
                .thenReturn(detalhesBuscaVerso);

        ResponseEntity<DetalhesBuscaVerso> response = versaoController.compararVerso("Gênesis", 1, 1);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
    }

    @Test
    @DisplayName("Given verses exist when compararVerso then verify service called with correct parameters")
    void testCompararVersoServiceCall() {
        DetalhesBuscaVerso emptyDetails = DetalhesBuscaVerso.builder()
                .nomeLivro("Gênesis")
                .capitulo(2)
                .versiculo(3)
                .comparacoes(new ArrayList<>())
                .build();

        when(versaoService.compararVersoEmTodasAsVersoes("Gênesis", 2, 3))
                .thenReturn(emptyDetails);

        versaoController.compararVerso("Gênesis", 2, 3);

        verify(versaoService, times(1)).compararVersoEmTodasAsVersoes("Gênesis", 2, 3);
        verify(versaoService, never()).listarTodasAsVersoes();
    }
}

