package com.example.orderservice.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDto {
    private Long id;
    private String name;
    private String type;
    private Double price;
}
