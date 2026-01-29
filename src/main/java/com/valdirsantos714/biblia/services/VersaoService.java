package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.DetalhesBuscaVerso;
import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.repositories.VersaoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VersaoService {

    @Autowired
    private VersaoRepository versaoRepository;

    @CircuitBreaker(name = "versaoService", fallbackMethod = "listarTodasAsVersoesFallback")
    @Retry(name = "versaoService")
    public List<VersaoDTO> listarTodasAsVersoes() {
        log.info("Executando método listarTodasAsVersoes() em VersaoService");
        return versaoRepository.findAllVersoes();
    }

    @CircuitBreaker(name = "versaoService", fallbackMethod = "compararVersoFallback")
    @Retry(name = "versaoService")
    public DetalhesBuscaVerso compararVersoEmTodasAsVersoes(String nomeLivro, Integer capitulo, Integer versiculo) {
        log.info("Executando método compararVersoEmTodasAsVersoes() para livro: {}, capítulo: {}, versículo: {}",
                nomeLivro, capitulo, versiculo);

        var comparacoes = versaoRepository.findComparacoesVerso(nomeLivro, capitulo, versiculo);

        return DetalhesBuscaVerso.builder()
                .nomeLivro(nomeLivro)
                .capitulo(capitulo)
                .versiculo(versiculo)
                .comparacoes(comparacoes)
                .build();
    }

    public List<VersaoDTO> listarTodasAsVersoesFallback(Exception e) {
        log.error("Método listarTodasAsVersoesFallback() sendo executado com a exceção: {}", e.getMessage());
        return List.of();
    }

    public DetalhesBuscaVerso compararVersoFallback(String nomeLivro, Integer capitulo, Integer versiculo, Exception e) {
        log.error("Método compararVersoFallback() sendo executado com a exceção: {} para livro: {}, capítulo: {}, versículo: {}",
                e.getMessage(), nomeLivro, capitulo, versiculo);
        return DetalhesBuscaVerso.builder()
                .nomeLivro(nomeLivro)
                .capitulo(capitulo)
                .versiculo(versiculo)
                .comparacoes(List.of())
                .build();
    }
}

