package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.repositories.VersiculosRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VersiculosService {

    @Autowired
    private VersiculosRepository VersiculoDoDiaRepository;

    @CircuitBreaker(name = "versiculosService", fallbackMethod = "findAllFallback")
    @Retry(name = "versiculosService")
    public List<VersiculoDoDia> findAll() {
        List<VersiculoDoDia> lista = VersiculoDoDiaRepository.findAll();
        return lista;
    }

    public List<VersiculoDoDia> findAllFallback(Exception e) {
        return List.of();
    }

    @CircuitBreaker(name = "versiculosService", fallbackMethod = "findByIdFallback")
    @Retry(name = "versiculosService")
    public VersiculoDoDia findById(Long id) {
        Optional<VersiculoDoDia> VersiculoDoDia = VersiculoDoDiaRepository.findById(id);
        return VersiculoDoDia.orElseThrow(() -> new RuntimeException("Erro"));
    }

    public VersiculoDoDia findByIdFallback(Long id, Exception e) {
        throw new RuntimeException("Serviço indisponível. Não foi possível encontrar o versículo com id: " + id);
    }

    @CircuitBreaker(name = "versiculosService", fallbackMethod = "saveFallback")
    @Retry(name = "versiculosService")
    @Transactional
    public VersiculoDoDia save (VersiculoDoDia VersiculoDoDiaDto) {
        return VersiculoDoDiaRepository.save(VersiculoDoDiaDto);
    }

    public VersiculoDoDia saveFallback(VersiculoDoDia VersiculoDoDiaDto, Exception e) {
        throw new RuntimeException("Serviço indisponível. Não foi possível salvar o versículo");
    }

    @CircuitBreaker(name = "versiculosService", fallbackMethod = "deleteFallback")
    @Retry(name = "versiculosService")
    @Transactional
    public void delete(Long id) {
        try {
            VersiculoDoDiaRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Erro");
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteFallback(Long id, Exception e) {
        throw new RuntimeException("Serviço indisponível. Não foi possível deletar o versículo com id: " + id);
    }
}
