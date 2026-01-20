package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.entities.biblia.Versos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersosRepository extends JpaRepository<Versos, Long> {

    @Query("""
        SELECT DISTINCT v.versiculo
        FROM Versos v
        WHERE v.livro.id = :idLivro
          AND v.capitulo = :capitulo
        ORDER BY v.versiculo
    """)
    List<Integer> findVersiculosByLivroIdAndCapitulo(
            @Param("idLivro") Long idLivro,
            @Param("capitulo") Integer capitulo
    );

    @Query("""
        SELECT new com.valdirsantos714.biblia.dtos.VersosDTO(
            v.id,
            new com.valdirsantos714.biblia.dtos.VersaoDTO(
                ver.id,
                ver.nome
            ),
            new com.valdirsantos714.biblia.dtos.LivrosDTO(
                l.id,
                l.nome,
                l.abreviacao,
                new com.valdirsantos714.biblia.dtos.TestamentoDTO(
                    t.id,
                    t.nome
                )
            ),
            v.capitulo,
            v.versiculo,
            v.texto,
            v.testamento
        )
        FROM Versos v
        JOIN v.versao ver
        JOIN v.livro l
        JOIN l.testamento t
        WHERE l.id = :idLivro
          AND v.capitulo = :capitulo
          AND LOWER(ver.nome) = LOWER(:nomeVersao)
        ORDER BY v.versiculo ASC
    """)
    List<VersosDTO> findByLivroIdAndCapituloAndVersaoNome(
            @Param("idLivro") Long idLivro,
            @Param("capitulo") Integer capitulo,
            @Param("nomeVersao") String nomeVersao
    );

    @Query("""
        SELECT new com.valdirsantos714.biblia.dtos.VersosDTO(
            v.id,
            new com.valdirsantos714.biblia.dtos.VersaoDTO(ver.id, ver.nome),
            new com.valdirsantos714.biblia.dtos.LivrosDTO(
                l.id,
                l.nome,
                l.abreviacao,
                new com.valdirsantos714.biblia.dtos.TestamentoDTO(t.id, t.nome)
            ),
            v.capitulo,
            v.versiculo,
            v.texto,
            v.testamento
        )
        FROM Versos v
        JOIN v.versao ver
        JOIN v.livro l
        JOIN l.testamento t
        WHERE v.capitulo = :capitulo
          AND LOWER(ver.nome) = LOWER(:nomeVersao)
          AND (
                function(
                    'translate',
                    LOWER(l.nome),
                    'áàãâäéèêëíìîïóòõôöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc'
                ) =
                function(
                    'translate',
                    LOWER(:livro),
                    'áàãâäéèêëíìîïóòõôöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc'
                )
            OR
                function(
                    'translate',
                    LOWER(l.abreviacao),
                    'áàãâäéèêëíìîïóòõôöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc'
                ) =
                function(
                    'translate',
                    LOWER(:livro),
                    'áàãâäéèêëíìîïóòõôöúùûüç',
                    'aaaaaeeeeiiiiooooouuuuc'
                )
          )
        ORDER BY v.versiculo ASC
        """)
    List<VersosDTO> findByLivroNomeOuAbreviacaoAndCapituloAndVersao(
            @Param("livro") String livro,
            @Param("capitulo") Integer capitulo,
            @Param("nomeVersao") String nomeVersao
    );

    @Query("""
    SELECT DISTINCT v.capitulo
    FROM Versos v
    JOIN v.livro l
    WHERE
        (
            function(
                'translate',
                LOWER(l.nome),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            ) =
            function(
                'translate',
                LOWER(:livro),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            )
        OR
            function(
                'translate',
                LOWER(l.abreviacao),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            ) =
            function(
                'translate',
                LOWER(:livro),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            )
        )
    ORDER BY v.capitulo
""")
    List<Integer> findDistinctCapitulosByLivroNomeOuAbreviacao(
            @Param("livro") String livro
    );
}