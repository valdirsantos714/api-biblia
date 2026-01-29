package com.valdirsantos714.biblia.repositories;

import com.valdirsantos714.biblia.dtos.VersaoDTO;
import com.valdirsantos714.biblia.dtos.VersosDTO;
import com.valdirsantos714.biblia.dtos.VersoComparacoesDTO;
import com.valdirsantos714.biblia.entities.biblia.Versao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersaoRepository extends JpaRepository<Versao, Long> {

    @Query("""
        SELECT new com.valdirsantos714.biblia.dtos.VersaoDTO(
            v.id,
            v.nome
        )
        FROM Versao v
        ORDER BY v.nome
    """)
    List<VersaoDTO> findAllVersoes();

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
        WHERE (
            function(
                'translate',
                LOWER(l.nome),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            ) =
            function(
                'translate',
                LOWER(:nomeLivro),
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
                LOWER(:nomeLivro),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            )
        )
          AND v.capitulo = :capitulo
          AND v.versiculo = :versiculo
        ORDER BY ver.nome
    """)
    List<VersosDTO> findVersoByLivroCapituloVersiculoEmTodasAsVersoes(
            @Param("nomeLivro") String nomeLivro,
            @Param("capitulo") Integer capitulo,
            @Param("versiculo") Integer versiculo
    );

    @Query("""
        SELECT new com.valdirsantos714.biblia.dtos.VersoComparacoesDTO(
            ver.nome,
            v.texto
        )
        FROM Versos v
        JOIN v.versao ver
        JOIN v.livro l
        WHERE (
            function(
                'translate',
                LOWER(l.nome),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            ) =
            function(
                'translate',
                LOWER(:nomeLivro),
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
                LOWER(:nomeLivro),
                'áàãâäéèêëíìîïóòõôöúùûüç',
                'aaaaaeeeeiiiiooooouuuuc'
            )
        )
          AND v.capitulo = :capitulo
          AND v.versiculo = :versiculo
        ORDER BY ver.nome
    """)
    List<VersoComparacoesDTO> findComparacoesVerso(
            @Param("nomeLivro") String nomeLivro,
            @Param("capitulo") Integer capitulo,
            @Param("versiculo") Integer versiculo
    );
}
