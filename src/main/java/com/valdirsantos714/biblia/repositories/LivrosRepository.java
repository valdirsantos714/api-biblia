package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.entities.biblia.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivrosRepository extends JpaRepository<Livros, Long> {
}
