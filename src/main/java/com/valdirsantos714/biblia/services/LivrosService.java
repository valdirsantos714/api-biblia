package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.entities.biblia.Livros;
import com.valdirsantos714.biblia.repositories.LivrosRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivrosService {

    @Autowired
    private LivrosRepository livrosRepository;

    @CircuitBreaker(name = "livrosService", fallbackMethod = "listarFallback")
    @Retry(name = "livrosService")
    public List<Livros> listar () {
        return livrosRepository.findAll();
    }

    public List<Livros> listarFallback(Exception e) {
        return List.of();
    }

    @CircuitBreaker(name = "livrosService", fallbackMethod = "findByIdFallback")
    @Retry(name = "livrosService")
    public Livros findById(Long id) {
        Livros livro = livrosRepository.findById(id).orElseThrow(() -> new RuntimeException("Erro não tem esse livro"));
        return livro;
    }

    public Livros findByIdFallback(Long id, Exception e) {
        throw new RuntimeException("Serviço indisponível. Não foi possível encontrar o livro com id: " + id);
    }

    @CircuitBreaker(name = "livrosService", fallbackMethod = "pegaLivroPorNomeFallback")
    @Retry(name = "livrosService")
    public Livros pegaLivroPorNome(String nome) {
        Livros livro = listar().stream().filter(
                (l) -> l.getNome().equals(nome)).findFirst().orElseThrow(() -> new RuntimeException("Erro! Não existe nenhum livro na bíblia com esse nome!"));
        return livro;
    }

    public Livros pegaLivroPorNomeFallback(String nome, Exception e) {
        throw new RuntimeException("Serviço indisponível. Não foi possível encontrar o livro: " + nome);
    }
}
