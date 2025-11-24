package com.example.petservice.controller;

import com.example.petservice.domain.Pet;
import com.example.petservice.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetRepository repository;

    public PetController(PetRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Pet> findAll() {
        // converte Iterable em List
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .toList();
    }

    @PostMapping
    public Pet create(@RequestBody Pet pet) {
        return repository.save(pet);
    }
}