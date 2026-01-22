package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface VersiculosRepository extends JpaRepository<VersiculoDoDia, Long> {

    @Query(value = """
        SELECT vd.id, vd.id_verso
        FROM versiculo_do_dia vd
        WHERE vd.id_verso IN (
            SELECT v.id FROM versos v
            JOIN versao_biblia vb ON v.id_versao = vb.id
            WHERE LOWER(vb.nome) = 'nvt'
        )
        ORDER BY (
            ABS(EXTRACT(EPOCH FROM :data::date)::bigint) % (
                SELECT COUNT(*) FROM versos v
                JOIN versao_biblia vb ON v.id_versao = vb.id
                WHERE LOWER(vb.nome) = 'nvt'
            )
        )
        LIMIT 1
        """, nativeQuery = true)
    Optional<VersiculoDoDia> findVersiculoDoDiaAleatorioByData(@Param("data") LocalDate data);
}








