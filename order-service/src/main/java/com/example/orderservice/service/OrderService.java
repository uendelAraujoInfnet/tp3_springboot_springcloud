package com.example.orderservice.service;

import com.example.orderservice.api.CreateOrderRequest;
import com.example.orderservice.client.PetClient;
import com.example.orderservice.client.PetDto;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;
    private final PetClient petClient;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository itemRepository,
                        PetClient petClient) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.petClient = petClient;
    }

    public Mono<Order> createOrder(CreateOrderRequest request) {
        // 1) para cada item, ir no pet-service buscar o preço
        return Flux.fromIterable(request.getItems())
                .flatMap(itemReq ->
                        petClient.getPetById(itemReq.getPetId())
                                .map(pet -> new ItemWithPet(itemReq, pet))
                )
                .collectList()
                .flatMap(itemsWithPet -> {
                    // 2) calcular total
                    BigDecimal total = itemsWithPet.stream()
                            .map(i -> BigDecimal.valueOf(i.pet().getPrice())
                                    .multiply(BigDecimal.valueOf(i.req().getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    Order order = new Order(
                            request.getCustomerName(),
                            total,
                            LocalDateTime.now()
                    );

                    // 3) salvar Order e itens em Scheduler boundedElastic
                    return Mono.fromCallable(() -> orderRepository.save(order))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(savedOrder ->
                                    Flux.fromIterable(itemsWithPet)
                                            .flatMap(i -> {
                                                OrderItem item = new OrderItem(
                                                        savedOrder.getId(),
                                                        i.pet().getId(),
                                                        i.req().getQuantity(),
                                                        BigDecimal.valueOf(i.pet().getPrice())
                                                );
                                                return Mono.fromCallable(() -> itemRepository.save(item))
                                                        .subscribeOn(Schedulers.boundedElastic());
                                            })
                                            .then(Mono.just(savedOrder))
                            );
                });
    }

    public Mono<Order> findById(Long id) {
        return Mono.fromCallable(() -> orderRepository.findById(id).orElse(null))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<Order> findAll() {
        return Mono.fromCallable(orderRepository::findAllList)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }

    private record ItemWithPet(CreateOrderRequest.OrderItemRequest req, PetDto pet) {}
}
