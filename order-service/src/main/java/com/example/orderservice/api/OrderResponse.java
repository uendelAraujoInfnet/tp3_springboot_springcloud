package com.example.orderservice.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long id;
    private String customerName;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    @Getter
    @Setter
    public static class OrderItemResponse {
        private Long petId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
