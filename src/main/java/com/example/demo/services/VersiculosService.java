package com.example.demo.services;

import com.example.demo.entities.biblia.VersiculoDoDia;
import com.example.demo.repositories.VersiculosRepository;
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

    public List<VersiculoDoDia> findAll() {
        List<VersiculoDoDia> lista = VersiculoDoDiaRepository.findAll();
        return lista;
    }

    public VersiculoDoDia findById(Long id) {
        Optional<VersiculoDoDia> VersiculoDoDia = VersiculoDoDiaRepository.findById(id);
        return VersiculoDoDia.orElseThrow(() -> new RuntimeException("Erro"));
    }

    @Transactional
    public VersiculoDoDia save (VersiculoDoDia VersiculoDoDiaDto) {
        return VersiculoDoDiaRepository.save(VersiculoDoDiaDto);
    }

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
/*
    public VersiculoDoDia update (Long id, VersiculoDoDiaVersiculoDoDiaDto) {
        try {
            VersiculoDoDia VersiculoDoDia = VersiculoDoDiaRepository.getReferenceById(id);
            updateData(VersiculoDoDia, VersiculoDoDiaDto);

            return VersiculoDoDiaRepository.save(VersiculoDoDia);

        } catch (EntityNotFoundException e) {
            throw new RuntimeException(id);
        }
    }

    private void updateData(VersiculoDoDia outdateVersiculoDoDia, VersiculoDoDiaupdatedVersiculoDoDia) {
        outdateVersiculoDoDia.setName(updatedVersiculoDoDia.name());
        outdateVersiculoDoDia.setAge(updatedVersiculoDoDia.age());
        outdateVersiculoDoDia.setSex(updatedVersiculoDoDia.sex());
        outdateVersiculoDoDia.setEmail(updatedVersiculoDoDia.email());

    }*/

}
