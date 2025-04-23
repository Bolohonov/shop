package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.api.response.OrderResponse;
import org.example.shop.api.response.OrderStatus;
import org.example.shop.model.Item;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.ItemRepo;
import org.example.shop.repo.OrderRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final static String DEFAULT_CUSTOMER = "customer";

    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final OrderItemService orderItemService;

    public Mono<Integer> makeOrder(String sessionId) {
        return orderRepo.findOrderBySessionAndStatusContainsIgnoreCase(sessionId, OrderStatus.NEW.name())
                .switchIfEmpty(Mono.error(new IllegalStateException("Order not found")))
                .flatMap(order -> {
                    order.setStatus(OrderStatus.IN_PROGRESS.name());
                    return orderRepo.save(order);
                })
                .map(Order::getId);
    }

    public Mono<Map<Integer, Integer>> findOrderItemsMapBySession(String session) {
        return orderRepo.findOrderBySessionAndStatusContainsIgnoreCase(session, OrderStatus.NEW.name())
                .map(Order::getId)
                .flatMapMany(orderItemService::getByOrderId)
                .collectMap(OrderItem::getItemId, OrderItem::getQuantity);
    }

    public Mono<Void> updateOrder(int itemId, String actionName, String sessionId) {
        return itemRepo.findById(itemId)
                .zipWith(getOrCreate(sessionId))
                .flatMap(tuple -> {
                    Item item = tuple.getT1();
                    Order order = tuple.getT2();

                    return orderItemService.updateOrderItem(order, item, actionName);
                    });

    }


    public Flux<OrderResponse> getBySession(String session) {
        return orderRepo.findBySessionAndStatusNotContainsIgnoreCase(session, OrderStatus.NEW.name())
                .flatMap(orderItemService::getOrderResponseWithItems);
    }

    public Mono<OrderResponse> getById(int orderId) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found")))
                .flatMap(orderItemService::getOrderResponseWithItems);
    }

    private Mono<Order> getOrCreate(String session) {
        return orderRepo
                .findOrderBySessionAndStatusContainsIgnoreCase(session, OrderStatus.NEW.name())
                .switchIfEmpty(createNew(session));
    }

    private Mono<Order> createNew(String session) {
            Order newOrder = new Order();
            newOrder.setSession(session);
            newOrder.setCustomer(DEFAULT_CUSTOMER);
            newOrder.setStatus(OrderStatus.NEW.name());
            return orderRepo.save(newOrder);
    }
}
