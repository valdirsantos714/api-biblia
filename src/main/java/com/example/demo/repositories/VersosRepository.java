package com.example.demo.repositories;

import com.example.demo.entities.biblia.Versos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersosRepository extends JpaRepository<Versos, Long> {
}
