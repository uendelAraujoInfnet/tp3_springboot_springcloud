package com.example.orderservice;

import com.example.orderservice.api.CreateOrderRequest;
import com.example.orderservice.client.PetClient;
import com.example.orderservice.domain.Order;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("orderdb")
                    .withUsername("order_user")
                    .withPassword("order_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private PetClient petClient;

    @Test
    void shouldCreateOrder() {
        PetClient mock = Mockito.mock(PetClient.class);
        when(mock.getPetById(anyLong()))
                .thenReturn(Mono.just(new com.example.orderservice.client.PetDto() {{
                    setId(1L);
                    setName("Rex");
                    setType("dog");
                    setPrice(50.0);
                }}));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Cliente Teste");
        CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
        item.setPetId(1L);
        item.setQuantity(2);
        request.setItems(List.of(item));

        Mono<Order> result = orderService.createOrder(request);

        StepVerifier.create(result)
                .assertNext(order -> {})
                .verifyComplete();
    }
}
