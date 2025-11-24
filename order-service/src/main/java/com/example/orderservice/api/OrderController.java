package com.example.orderservice.api;

import com.example.orderservice.domain.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<Order> create(@RequestBody Mono<CreateOrderRequest> requestMono) {
        return requestMono.flatMap(service::createOrder);
    }

    @GetMapping("/{id}")
    public Mono<Order> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<Order> findAll() {
        return service.findAll();
    }
}
