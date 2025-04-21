package org.example.shop.repo;

import org.example.shop.model.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepo extends R2dbcRepository<Order, Integer> {

    Mono<Order> findBySessionAndStatus(String session, String status);

    Flux<Order> findBySessionAndStatusNot(String session, String status);
}
