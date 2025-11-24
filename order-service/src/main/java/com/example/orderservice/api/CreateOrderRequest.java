package com.example.orderservice.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    private String customerName;
    private List<OrderItemRequest> items;


    @Getter
    @Setter
    public static class OrderItemRequest {
        private Long petId;
        private Integer quantity;
    }
}
