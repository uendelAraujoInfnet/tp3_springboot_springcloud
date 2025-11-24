package com.example.orderservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PetClient {

    private final WebClient petWebClient;

    public PetClient(WebClient petWebClient) {
        this.petWebClient = petWebClient;
    }

    public Mono<PetDto> getPetById(Long id) {
        return petWebClient.get()
                .uri("/pets/{id}", id)
                .retrieve()
                .bodyToMono(PetDto.class);
    }
}
