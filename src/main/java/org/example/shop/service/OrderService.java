package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.api.request.OrderAction;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.api.response.OrderResponse;
import org.example.shop.api.response.OrderStatus;
import org.example.shop.dto.OrderDto;
import org.example.shop.dto.OrderItemDto;
import org.example.shop.mapper.OrderItemMapper;
import org.example.shop.model.Item;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.ItemRepo;
import org.example.shop.repo.OrderRepo;
import org.example.shop.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final static String DEFAULT_CUSTOMER = "customer";

    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final OrderItemService orderItemService;

    public Mono<Integer> makeOrder(String sessionId) {
        return orderRepo.findBySessionAndStatus(sessionId, OrderStatus.NEW.name())
                .switchIfEmpty(Mono.error(new IllegalStateException("Order not found")))
                .flatMap(order -> {
                    order.setStatus(OrderStatus.IN_PROGRESS.name());
                    return orderRepo.save(order);
                })
                .map(Order::getId);
    }

    @Transactional(readOnly = true)
    public Mono<Map<Integer, Integer>> findOrderItemsMapBySession(String session) {
        return orderRepo.findBySessionAndStatus(session, OrderStatus.NEW.name())
                .map(Order::getId)
                .flatMapMany(orderItemService::getByOrderId)
                .collectMap(OrderItem::getItemId, OrderItem::getQuantity);
    }

    @Transactional
    public Mono<Void> updateOrder(int itemId, String actionName, String sessionId) {
        return itemRepo.findById(itemId)
                .zipWith(getOrCreate(sessionId))
                .flatMap(tuple -> {
                    Item item = tuple.getT1();
                    Order order = tuple.getT2();

                    return orderItemService.updateOrderItem(order, item, actionName);
                    });

    }


    @Transactional(readOnly = true)
    public Flux<OrderResponse> getBySession(String session) {
        return orderRepo.findBySessionAndStatusNot(session, OrderStatus.NEW.name())
                .flatMap(orderItemService::getOrderResponseWithItems);
    }

    @Transactional(readOnly = true)
    public Mono<OrderResponse> getById(int orderId) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found")))
                .flatMap(orderItemService::getOrderResponseWithItems);
    }

    private Mono<Order> getOrCreate(String session) {
        return orderRepo
                .findBySessionAndStatus(session, OrderStatus.NEW.name())
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
