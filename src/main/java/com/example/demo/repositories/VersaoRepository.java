package com.example.demo.repositories;

import com.example.demo.entities.biblia.Versao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersaoRepository extends JpaRepository<Versao, Long> {
}
