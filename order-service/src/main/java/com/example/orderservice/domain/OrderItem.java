package com.example.orderservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    private Long id;

    private Long orderId;
    private Long petId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItem(Long orderId, Long petId, Integer quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.petId = petId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
