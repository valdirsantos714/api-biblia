package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.entities.biblia.Testamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestamentoRepository extends JpaRepository<Testamento, Long> {
}
