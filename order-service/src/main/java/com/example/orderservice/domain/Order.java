package com.example.orderservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("orders")
@Getter
@Setter
public class Order {

    @Id
    private Long id;

    private String customerName;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    public Order(String customerName, BigDecimal totalAmount, LocalDateTime createdAt) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }
}
