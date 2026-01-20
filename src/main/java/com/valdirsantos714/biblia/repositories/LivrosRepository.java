package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.dtos.LivrosDTO;
import com.valdirsantos714.biblia.entities.biblia.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivrosRepository extends JpaRepository<Livros, Long> {
    @Query("""
        SELECT new com.valdirsantos714.biblia.dtos.LivrosDTO(
            l.id,
            l.nome,
            l.abreviacao,
            new com.valdirsantos714.biblia.dtos.TestamentoDTO(
                t.id,
                t.nome
            )
        )
        FROM Livros l
        JOIN l.testamento t
        ORDER BY l.id
    """)
    List<LivrosDTO> listarLivrosComTestamento();
}
