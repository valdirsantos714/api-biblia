package com.example.demo.services;

import com.example.demo.entities.biblia.Livros;
import com.example.demo.repositories.LivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivrosService {

    @Autowired
    private LivrosRepository livrosRepository;

    public List<Livros> listar () {
        return livrosRepository.findAll();
    }

    public Livros findById(Long id) {
        Livros livro = livrosRepository.findById(id).orElseThrow(() -> new RuntimeException("Erro não tem esse livro"));
        return livro;
    }

    public Livros pegaLivroPorNome(String nome) {
        Livros livro = listar().stream().filter(
                (l) -> l.getNome().equals(nome)).findFirst().orElseThrow(() -> new RuntimeException("Erro! Não existe nenhum livro na bíblia com esse nome!"));
        return livro;
    }
}
