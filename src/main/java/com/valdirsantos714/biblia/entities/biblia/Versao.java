package com.valdirsantos714.biblia.entities.biblia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

//@AttributeOverride(name = "id", column = @Column(name = "ID_VERSAO"))

@Entity
@Table(name = "versao_biblia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Versao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "versao")
    private Set<Versos> listVersos = new HashSet<>();


}
