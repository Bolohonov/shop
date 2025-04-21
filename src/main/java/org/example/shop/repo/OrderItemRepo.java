package org.example.shop.repo;

import org.example.shop.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepo extends R2dbcRepository<OrderItem, Integer> {

    Flux<OrderItem> findByOrderId(Integer orderId);

    Mono<OrderItem> findByOrderIdAndItemId(Integer orderId, Integer itemId);
}
