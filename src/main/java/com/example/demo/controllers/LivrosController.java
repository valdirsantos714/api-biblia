package com.example.demo.controllers;

import com.example.demo.entities.biblia.Livros;
import com.example.demo.entities.biblia.Versos;
import com.example.demo.services.LivrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/livros")
@CrossOrigin("*")
public class LivrosController {

    @Autowired
    private LivrosService livrosService;

    @GetMapping("/all")
    public ResponseEntity<List<Livros>> findAll() {
        List<Livros> list = livrosService.listar();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id_livro}/{capitulo}")
    public ResponseEntity<Set<Versos>> amostraVersos(@PathVariable(name = "id_livro") Long id_livro, @PathVariable(name = "capitulo") Integer capitulo) {

        Livros livro = livrosService.findById(id_livro);

        Set<Versos> list = livro.getListVersos().stream()
                .filter(verso -> verso.getCapitulo() == capitulo) // Filtrar versos pelo capítulo desejado
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(list);

    }



    @GetMapping("/{idLivro}")
    public ResponseEntity<Set<Integer>> mostraNumeroDeCapitulosPeloId(@PathVariable(name = "idLivro") Long id) {

        Livros livro = livrosService.findById(id);

        Set<Integer> numeroDeCapitulosDoLivro = livro.getListVersos().stream().map(Versos::getCapitulo).collect(Collectors.toSet());

        return ResponseEntity.ok().body(numeroDeCapitulosDoLivro);

    }

    @GetMapping("/{livro}/{capitulo}/verNumeroDeVersos")
    public ResponseEntity<List<Integer>> verNumeroDeVersos(@PathVariable(name = "livro") Long idLivro, @PathVariable(name = "capitulo") Integer capitulo) {
        Livros livro = livrosService.findById(idLivro);

        List<Integer> numeroDeVersiculos = livro.getListVersos().stream().filter((v) -> v.getCapitulo() == capitulo).map(Versos::getVersiculo).collect(Collectors.toList());

        return ResponseEntity.ok().body(numeroDeVersiculos);
    }

}

