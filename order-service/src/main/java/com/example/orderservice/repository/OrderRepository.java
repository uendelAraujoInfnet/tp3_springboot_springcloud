package com.example.orderservice.repository;

import com.example.orderservice.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.StreamSupport;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    default List<Order> findAllList() {
        return StreamSupport.stream(findAll().spliterator(), false)
                .toList();
    }
}
