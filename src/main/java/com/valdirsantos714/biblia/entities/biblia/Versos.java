package com.valdirsantos714.biblia.entities.biblia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "versos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Versos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_versao")
    private Versao versao;

    @ManyToOne
    @JoinColumn(name = "id_livro")
    private Livros livro;

    @Column(nullable = false)
    private Integer capitulo;

    @Column(nullable = false)
    private Integer versiculo;

    @Column(nullable = false, columnDefinition = "text")
    private String texto;

    @Column(nullable = true)
    private Integer testamento;

    @JsonIgnore
    @OneToMany(mappedBy = "verso")
    private Set<VersiculoDoDia> versiculosDoDia = new HashSet<>();


}
