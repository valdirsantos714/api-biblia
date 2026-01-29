package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.*;
import com.valdirsantos714.biblia.repositories.VersaoRepository;
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
@DisplayName("VersaoService Tests")
class VersaoServiceTest {

    @Mock
    private VersaoRepository versaoRepository;

    @InjectMocks
    private VersaoService versaoService;

    private VersaoDTO versaoDTO1;
    private VersaoDTO versaoDTO2;
    private VersaoDTO versaoDTO3;
    private VersoComparacoesDTO versoComparacoesDTO1;
    private VersoComparacoesDTO versoComparacoesDTO2;
    private VersoComparacoesDTO versoComparacoesDTO3;

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
    }

    @Test
    @DisplayName("Should list all available versions successfully")
    void testListarTodasAsVersoes() {
        List<VersaoDTO> versoesList = new ArrayList<>();
        versoesList.add(versaoDTO1);
        versoesList.add(versaoDTO2);
        versoesList.add(versaoDTO3);

        when(versaoRepository.findAllVersoes()).thenReturn(versoesList);

        List<VersaoDTO> resultado = versaoService.listarTodasAsVersoes();

        assertEquals(3, resultado.size());
        assertEquals("NVT", resultado.get(0).getNome());
        assertEquals("NVI", resultado.get(1).getNome());
        assertEquals("ARA", resultado.get(2).getNome());
        verify(versaoRepository, times(1)).findAllVersoes();
    }

    @Test
    @DisplayName("Should return empty list when no versions found")
    void testListarTodasAsVersoesVazio() {
        when(versaoRepository.findAllVersoes()).thenReturn(new ArrayList<>());

        List<VersaoDTO> resultado = versaoService.listarTodasAsVersoes();

        assertTrue(resultado.isEmpty());
        verify(versaoRepository, times(1)).findAllVersoes();
    }

    @Test
    @DisplayName("Should return single version in list")
    void testListarTodasAsVersoesSingleVersion() {
        List<VersaoDTO> versoesList = new ArrayList<>();
        versoesList.add(versaoDTO1);

        when(versaoRepository.findAllVersoes()).thenReturn(versoesList);

        List<VersaoDTO> resultado = versaoService.listarTodasAsVersoes();

        assertEquals(1, resultado.size());
        assertEquals("NVT", resultado.get(0).getNome());
        verify(versaoRepository, times(1)).findAllVersoes();
    }

    @Test
    @DisplayName("Should compare verse in all versions successfully")
    void testCompararVersoEmTodasAsVersoes() {
        List<VersoComparacoesDTO> comparacoesList = new ArrayList<>();
        comparacoesList.add(versoComparacoesDTO1);
        comparacoesList.add(versoComparacoesDTO2);
        comparacoesList.add(versoComparacoesDTO3);

        when(versaoRepository.findComparacoesVerso("Gênesis", 1, 1))
                .thenReturn(comparacoesList);

        DetalhesBuscaVerso resultado = versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1);

        assertNotNull(resultado);
        assertEquals("Gênesis", resultado.getNomeLivro());
        assertEquals(Integer.valueOf(1), resultado.getCapitulo());
        assertEquals(Integer.valueOf(1), resultado.getVersiculo());
        assertEquals(3, resultado.getComparacoes().size());
        assertEquals("NVT", resultado.getComparacoes().get(0).getVersao());
        assertEquals("NVI", resultado.getComparacoes().get(1).getVersao());
        assertEquals("ARA", resultado.getComparacoes().get(2).getVersao());
        verify(versaoRepository, times(1)).findComparacoesVerso("Gênesis", 1, 1);
    }

    @Test
    @DisplayName("Should return empty comparacoes list when verse not found in any version")
    void testCompararVersoEmTodasAsVersoesNaoEncontrado() {
        when(versaoRepository.findComparacoesVerso("Inexistente", 999, 999))
                .thenReturn(new ArrayList<>());

        DetalhesBuscaVerso resultado = versaoService.compararVersoEmTodasAsVersoes("Inexistente", 999, 999);

        assertNotNull(resultado);
        assertEquals("Inexistente", resultado.getNomeLivro());
        assertTrue(resultado.getComparacoes().isEmpty());
        verify(versaoRepository, times(1)).findComparacoesVerso("Inexistente", 999, 999);
    }

    @Test
    @DisplayName("Should compare verse with book name case insensitive")
    void testCompararVersoComNomeLivroCase() {
        List<VersoComparacoesDTO> comparacoesList = new ArrayList<>();
        comparacoesList.add(versoComparacoesDTO1);
        comparacoesList.add(versoComparacoesDTO2);

        when(versaoRepository.findComparacoesVerso("GÊNESIS", 1, 1))
                .thenReturn(comparacoesList);

        DetalhesBuscaVerso resultado = versaoService.compararVersoEmTodasAsVersoes("GÊNESIS", 1, 1);

        assertEquals(2, resultado.getComparacoes().size());
        verify(versaoRepository, times(1)).findComparacoesVerso("GÊNESIS", 1, 1);
    }

    @Test
    @DisplayName("Should return verse with correct metadata")
    void testCompararVersoMetadata() {
        List<VersoComparacoesDTO> comparacoesList = new ArrayList<>();
        comparacoesList.add(versoComparacoesDTO1);

        when(versaoRepository.findComparacoesVerso("Gênesis", 1, 1))
                .thenReturn(comparacoesList);

        DetalhesBuscaVerso resultado = versaoService.compararVersoEmTodasAsVersoes("Gênesis", 1, 1);

        assertEquals("Gênesis", resultado.getNomeLivro());
        assertEquals(Integer.valueOf(1), resultado.getCapitulo());
        assertEquals(Integer.valueOf(1), resultado.getVersiculo());
        assertEquals(1, resultado.getComparacoes().size());
        assertEquals("NVT", resultado.getComparacoes().get(0).getVersao());
        assertEquals("No princípio, criou Deus os céus e a terra.", resultado.getComparacoes().get(0).getTexto());
    }

    @Test
    @DisplayName("Should handle multiple verses in different chapters")
    void testCompararVersoCapitulosDiferentes() {
        List<VersoComparacoesDTO> comparacoesList = new ArrayList<>();
        comparacoesList.add(VersoComparacoesDTO.builder()
                .versao("NVT")
                .texto("E descansou no sétimo dia.")
                .build());

        when(versaoRepository.findComparacoesVerso("Gênesis", 2, 1))
                .thenReturn(comparacoesList);

        DetalhesBuscaVerso resultado = versaoService.compararVersoEmTodasAsVersoes("Gênesis", 2, 1);

        assertEquals(Integer.valueOf(2), resultado.getCapitulo());
        assertEquals(1, resultado.getComparacoes().size());
    }

    @Test
    @DisplayName("Should trigger fallback when exception occurs in listarTodasAsVersoes")
    void testListarTodasAsVersoesFallback() {
        List<VersaoDTO> resultado = versaoService.listarTodasAsVersoesFallback(new RuntimeException("Database error"));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Should trigger fallback when exception occurs in compararVersoEmTodasAsVersoes")
    void testCompararVersoFallback() {
        DetalhesBuscaVerso resultado = versaoService.compararVersoFallback("Gênesis", 1, 1,
                new RuntimeException("Database error"));

        assertNotNull(resultado);
        assertEquals("Gênesis", resultado.getNomeLivro());
        assertEquals(Integer.valueOf(1), resultado.getCapitulo());
        assertEquals(Integer.valueOf(1), resultado.getVersiculo());
        assertTrue(resultado.getComparacoes().isEmpty());
    }
}

