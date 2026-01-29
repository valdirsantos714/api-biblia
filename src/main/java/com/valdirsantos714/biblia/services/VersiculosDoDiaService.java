package com.valdirsantos714.biblia.services;

import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.dtos.TestamentoDTO;
import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import com.valdirsantos714.biblia.repositories.VersiculosRepository;
import com.valdirsantos714.biblia.repositories.VersosRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VersiculosDoDiaService {

    @Autowired
    private VersiculosRepository VersiculoDoDiaRepository;

    @Autowired
    private VersosRepository versosRepository;

    @CircuitBreaker(name = "versiculoDoDiaService", fallbackMethod = "findAllFallback")
    @Retry(name = "versiculoDoDiaService")
    public List<VersiculoDoDia> findAll() {
        log.info("Executando método findAll() em VersiculosDoDiaService");
        return VersiculoDoDiaRepository.findAll();
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

    @CircuitBreaker(name = "versiculoDoDiaService", fallbackMethod = "findVersoAleatorioDoDiaFallback")
    @Retry(name = "versiculoDoDiaService")
    public VersosDTO findVersoAleatorioDoDia(LocalDate data) {
        log.info("Executando método findVersoAleatorioDoDia() em VersiculosDoDiaService para data: {}", data);

        com.valdirsantos714.biblia.entities.biblia.Versos verso = versosRepository.findVersoAleatorioDoDia(data);
        if (verso == null) {
            throw new RuntimeException("Nenhum versículo encontrado para a data: " + data);
        }

        // Convertendo a entidade Versos para VersosDTO
        return converterVersoParaDTO(verso);
    }

    private VersosDTO converterVersoParaDTO(com.valdirsantos714.biblia.entities.biblia.Versos verso) {
        return new VersosDTO(
            verso.getId(),
            new com.valdirsantos714.biblia.dtos.VersaoDTO(verso.getVersao().getId(), verso.getVersao().getNome()),
            new com.valdirsantos714.biblia.dtos.LivrosDTO(
                verso.getLivro().getId(),
                verso.getLivro().getNome(),
                verso.getLivro().getAbreviacao(),
                new com.valdirsantos714.biblia.dtos.TestamentoDTO(verso.getLivro().getTestamento().getId(), verso.getLivro().getTestamento().getNome())
            ),
            verso.getCapitulo(),
            verso.getVersiculo(),
            verso.getTexto(),
            verso.getTestamento()
        );
    }

    public VersosDTO findVersoAleatorioDoDiaFallback(LocalDate data, Exception e) {
        log.warn("Método findVersoAleatorioDoDiaFallback em VersiculosDoDiaService sendo executado para data: {}", data);
        throw new RuntimeException("Serviço indisponível. Não foi possível encontrar o versículo aleatório do dia para a data: " + data);
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


