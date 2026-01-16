package com.valdirsantos714.biblia.entities.biblia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livros")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Livros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(70)")
    private String nome;

    @Column(nullable = true, columnDefinition = "varchar(10)")
    private String abreviacao;

    @ManyToOne
    @JoinColumn(name = "id_testamento")
    private Testamento testamento;

    @JsonIgnore
    @OneToMany(mappedBy = "livro")
    private Set<Versos> listVersos = new HashSet<>();

}
