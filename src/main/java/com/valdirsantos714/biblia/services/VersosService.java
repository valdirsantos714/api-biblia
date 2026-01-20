package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.repositories.VersosRepository;
import com.valdirsantos714.biblia.utils.TextoUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VersosService {

    @Autowired
    private VersosRepository versosRepository;

    @CircuitBreaker(name = "versosService", fallbackMethod = "buscarVersosPorCapituloFallback")
    @Retry(name = "versosService")
    public List<VersosDTO> buscarVersosPorCapitulo(
            Long idLivro,
            Integer capitulo) {
        log.info("Executando método buscarVersosPorCapitulo() para livro id: {} e capítulo: {}", idLivro, capitulo);

        String versaoPadrao = "nvt";
        return versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, versaoPadrao);
    }

    public List<VersosDTO> buscarVersosPorCapituloFallback(Long idLivro, Integer capitulo, Exception e) {
        log.warn("Método buscarVersosPorCapituloFallback sendo executado para livro id: {} capítulo: {} com exceção: {}", idLivro, capitulo, e.getMessage());
        throw new RuntimeException("Serviço indisponível. Não foi possível buscar os versos do capítulo: " + capitulo);
    }

    @CircuitBreaker(name = "versosService", fallbackMethod = "buscarNumerosDeVersosFallback")
    @Retry(name = "versosService")
    public List<Integer> buscarNumerosDeVersos(
            Long idLivro,
            Integer capitulo) {
        log.info("Executando método buscarNumerosDeVersos() para livro id: {} e capítulo: {}", idLivro, capitulo);

        return versosRepository.findVersiculosByLivroIdAndCapitulo(idLivro, capitulo);
    }

    public List<Integer> buscarNumerosDeVersosFallback(Long idLivro, Integer capitulo, Exception e) {
        log.warn("Método buscarNumerosDeVersosFallback sendo executado para livro id: {} capítulo: {} com exceção: {}", idLivro, capitulo, e.getMessage());
        throw new RuntimeException("Serviço indisponível. Não foi possível buscar os versos do capítulo: " + capitulo);
    }

    @CircuitBreaker(name = "versosService", fallbackMethod = "buscarVersosPorCapituloEVersaoFallback")
    @Retry(name = "versosService")
    public List<VersosDTO> buscarVersosPorCapituloEVersao(
            Long idLivro,
            Integer capitulo,
            String nomeVersao) {
        log.info("Executando método buscarVersosPorCapituloEVersao() para livro id: {}, capítulo: {} e versão: {}", idLivro, capitulo, nomeVersao);

        return versosRepository.findByLivroIdAndCapituloAndVersaoNome(idLivro, capitulo, nomeVersao);
    }

    public List<VersosDTO> buscarVersosPorCapituloEVersaoFallback(Long idLivro, Integer capitulo, String nomeVersao, Exception e) {
        log.warn("Método buscarVersosPorCapituloEVersaoFallback sendo executado para livro id: {}, capítulo: {}, versão: {} com exceção: {}", idLivro, capitulo, nomeVersao, e.getMessage());
        throw new RuntimeException("Serviço indisponível. Não foi possível buscar os versos do capítulo: " + capitulo);
    }

    @CircuitBreaker(name = "versosService", fallbackMethod = "buscarVersosPorLivroNomeEVersaoFallback")
    @Retry(name = "versosService")
    public List<VersosDTO> buscarVersosPorLivroNomeEVersao(
            String livro,
            Integer capitulo,
            String nomeVersao
    ) {
        log.info("Executando método buscarVersosPorLivroNomeEVersao() para livro: {}, capítulo: {} e versão: {}", livro, capitulo, nomeVersao);
        String livroNormalizado = TextoUtils.normalizar(livro);

        return versosRepository.findByLivroNomeOuAbreviacaoAndCapituloAndVersao(
                livroNormalizado,
                capitulo,
                nomeVersao
        );
    }

    public List<VersosDTO> buscarVersosPorLivroNomeEVersaoFallback(
            String livro,
            Integer capitulo,
            String nomeVersao,
            Exception e
    ) {
        log.warn("Método buscarVersosPorLivroNomeEVersaoFallback sendo executado para livro: {}, capítulo: {}, versão: {} com exceção: {}", livro, capitulo, nomeVersao, e.getMessage());
        throw new RuntimeException("Serviço indisponível. Não foi possível buscar os versos do capítulo: " + capitulo);
    }

    @CircuitBreaker(name = "versosService", fallbackMethod = "buscarCapitulosPorLivroFallback")
    @Retry(name = "versosService")
     public List<Integer> buscarCapitulosPorLivro(String livro) {
        log.info("Buscando capítulos do livro: {}", livro);

        String livroNormalizado = TextoUtils.normalizar(livro);

        return versosRepository.findDistinctCapitulosByLivroNomeOuAbreviacao(
                livroNormalizado
        );
    }

    public List<Integer> buscarCapitulosPorLivroFallback(String livro, Exception e) {
        log.warn("Método buscarCapitulosPorLivroFallback sendo executado para livro: {} com exceção: {}", livro, e.getMessage());
        throw new RuntimeException("Serviço indisponível. Não foi possível buscar os capítulos do livro: " + livro);
    }
}
