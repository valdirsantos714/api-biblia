package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.repositories.LivrosRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LivrosService {

    @Autowired
    private LivrosRepository livrosRepository;

    @CircuitBreaker(name = "livrosService", fallbackMethod = "listarFallback")
    @Retry(name = "livrosService")
    public List<LivrosDTO> listar() {
        log.info("Executando método listar() em LivrosService");
        return livrosRepository.listarLivrosComTestamento();
    }

    public List<LivrosDTO> listarFallback(Exception e) {
        log.error("Método listarFallback() sendo executado: com a exceção {}", e.getMessage());
        return List.of();
    }
}
