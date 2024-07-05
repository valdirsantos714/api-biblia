package com.example.demo.repositories;

import com.example.demo.entities.biblia.VersiculoDoDia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersiculosRepository extends JpaRepository<VersiculoDoDia, Long> {
}
