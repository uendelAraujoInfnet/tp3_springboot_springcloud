package com.example.petservice.repository;

import com.example.petservice.domain.Pet;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository  extends CrudRepository<Pet, Long> {}
