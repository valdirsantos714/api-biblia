package com.example.demo.repositories;

import com.example.demo.entities.biblia.Testamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestamentoRepository extends JpaRepository<Testamento, Long> {
}
