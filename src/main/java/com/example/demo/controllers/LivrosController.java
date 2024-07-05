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
        //Não se coloca setcapitulo mas sim get capitulo == capitulo pra pegar só os versos desse capitulo que pedi em cima

        return ResponseEntity.ok().body(list);

    } // este endpoint está funcionando corretamente



    @GetMapping("/{idLivro}")
    public ResponseEntity<Set<Integer>> mostraNumeroDeCapitulosPeloId(@PathVariable(name = "idLivro") Long id) {

        Livros livro = livrosService.findById(id);

        Set<Integer> numeroDeCapitulosDoLivro = livro.getListVersos().stream().map(Versos::getCapitulo).collect(Collectors.toSet());

        return ResponseEntity.ok().body(numeroDeCapitulosDoLivro);

    } //Este endpoint está funcionando também mas só pode usar ou o de procurar por nome ou o de id*/

    @GetMapping("/{livro}/{capitulo}/verNumeroDeVersos")
    public ResponseEntity<List<Integer>> verNumeroDeVersos(@PathVariable(name = "livro") Long idLivro, @PathVariable(name = "capitulo") Integer capitulo) {
        Livros livro = livrosService.findById(idLivro);

        List<Integer> numeroDeVersiculos = livro.getListVersos().stream().filter((v) -> v.getCapitulo() == capitulo).map(Versos::getVersiculo).collect(Collectors.toList());

        return ResponseEntity.ok().body(numeroDeVersiculos);
    }



    /*@GetMapping("/{livro}/{capitulo}/{versiculo}") //tem que ajeitar a parte que pega os capitulos
    public ResponseEntity<Versos> tudo(@PathVariable(name = "livro") String nomeLivro, @PathVariable(name = "capitulo") Integer capitulo, @PathVariable(name = "versiculo") String texto) {
        Livros livro = livrosService.pegaLivroPorNome(nomeLivro);

        Versos listaCerta = livro.getListVersos().stream().filter(
                (v) -> v.getCapitulo() == capitulo && v.getTexto().equals(texto)).findFirst().orElseThrow(() -> new RuntimeException("Deu erro aqui pra achar o verso por nome de livro"));

        return ResponseEntity.ok().body(listaCerta);
    }*/ //Esse endpoint não está retornando o verso escrito na url


    /*@GetMapping("/{nomeLivro}")
    public ResponseEntity<Set<Integer>> mostraNumeroDeCapitulosPeloNomeDoLivro(@PathVariable(name = "nomeLivro") String nome) {

        Livros livro = livrosService.pegaLivroPorNome(nome);

        Set<Integer> numeroDeCapitulosDoLivro = livro.getListVersos().stream().map(Versos::getCapitulo).collect(Collectors.toSet());

        return ResponseEntity.ok().body(numeroDeCapitulosDoLivro);
    }*/
}

