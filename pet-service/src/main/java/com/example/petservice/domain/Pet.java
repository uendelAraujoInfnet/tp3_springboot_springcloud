package com.example.petservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("pets")
@Getter @Setter
public class Pet {

    @Id
    private Long id;

    private String name;
    private String type;
    private Double price;

    public Pet() {}

    public Pet(Long id, String name, String type, Double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

}