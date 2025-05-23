package org.example.showcase.repo;

import org.example.showcase.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepo extends R2dbcRepository<OrderItem, Long> {

    Flux<OrderItem> findByOrderId(Long orderId);

    Mono<OrderItem> findByOrderIdAndItemId(Long orderId, Long itemId);
}
