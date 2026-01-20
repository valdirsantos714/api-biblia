package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.repositories.VersiculosRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VersiculosDoDiaService {

    @Autowired
    private VersiculosRepository VersiculoDoDiaRepository;

    @CircuitBreaker(name = "versiculoDoDiaService", fallbackMethod = "findAllFallback")
    @Retry(name = "versiculoDoDiaService")
    public List<VersiculoDoDia> findAll() {
        log.info("Executando método findAll() em VersiculosDoDiaService");
        List<VersiculoDoDia> lista = VersiculoDoDiaRepository.findAll();
        return lista;
    }

    public List<VersiculoDoDia> findAllFallback(Exception e) {
        log.warn("Método findAllFallback em VersiculosDoDiaService sendo executado");
        return List.of();
    }

    @CircuitBreaker(name = "versiculoDoDiaService", fallbackMethod = "findByIdFallback")
    @Retry(name = "versiculoDoDiaService")
    public VersiculoDoDia findById(Long id) {
        log.info("Executando método findById() em VersiculosDoDiaService para id: {}", id);

        Optional<VersiculoDoDia> VersiculoDoDia = VersiculoDoDiaRepository.findById(id);
        return VersiculoDoDia.orElseThrow(() -> new RuntimeException("Erro"));
    }

    public VersiculoDoDia findByIdFallback(Long id, Exception e) {
        log.info("Método findByIdFallback em VersiculosDoDiaService sendo executado");
        throw new RuntimeException("Serviço indisponível. Não foi possível encontrar o versículo com id: " + id);
    }

    @CircuitBreaker(name = "versiculoDoDiaService", fallbackMethod = "saveFallback")
    @Retry(name = "versiculoDoDiaService")
    @Transactional
    public VersiculoDoDia save (VersiculoDoDia VersiculoDoDiaDto) {
        log.info("Executando método save() em VersiculosDoDiaService com o versículo: {}", VersiculoDoDiaDto);
        return VersiculoDoDiaRepository.save(VersiculoDoDiaDto);
    }

    public VersiculoDoDia saveFallback(VersiculoDoDia VersiculoDoDiaDto, Exception e) {
        log.info("Método saveFallback em VersiculosDoDiaService sendo executado");
        throw new RuntimeException("Serviço indisponível. Não foi possível salvar o versículo");
    }

    @CircuitBreaker(name = "versiculoDoDiaService", fallbackMethod = "deleteFallback")
    @Retry(name = "versiculoDoDiaService")
    @Transactional
    public void delete(Long id) {
        try {
            log.info("Executando método delete() em VersiculosDoDiaService para id: {}", id);
            VersiculoDoDiaRepository.deleteById(id);

        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Erro");
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteFallback(Long id, Exception e) {
        log.info("Método deleteFallback em VersiculosDoDiaService sendo executado");
        throw new RuntimeException("Serviço indisponível. Não foi possível deletar o versículo com id: " + id);
    }
}
