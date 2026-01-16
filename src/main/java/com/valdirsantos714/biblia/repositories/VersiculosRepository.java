package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersiculosRepository extends JpaRepository<VersiculoDoDia, Long> {
}
