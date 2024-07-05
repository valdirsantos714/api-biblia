package com.example.demo.entities.biblia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "testamento")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Testamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(70)")
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "testamento")
    private Set<Livros> livros = new HashSet<>();

}
