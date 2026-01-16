package com.valdirsantos714.biblia.controllers;


import com.valdirsantos714.biblia.entities.biblia.VersiculoDoDia;
import com.valdirsantos714.biblia.services.VersiculosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/versiculos")
public class VersiculoDoDiaController {

    @Autowired
    private VersiculosService versiculosService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<VersiculoDoDia>> findAllVersiculoDoDias() {
        List<VersiculoDoDia> list = versiculosService.findAll();

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VersiculoDoDia> findByIdVersiculoDoDia (@PathVariable Long id) {
        VersiculoDoDia VersiculoDoDia = versiculosService.findById(id);

        return ResponseEntity.ok().body(VersiculoDoDia);

    }

    @PostMapping
    public ResponseEntity<VersiculoDoDia> saveVerso(@RequestBody VersiculoDoDia versiculoDoDia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(versiculosService.save(versiculoDoDia));
    }



}
