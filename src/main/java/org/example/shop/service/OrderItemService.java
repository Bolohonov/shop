package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.api.request.OrderAction;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.api.response.OrderResponse;
import org.example.shop.model.Item;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.ItemRepo;
import org.example.shop.repo.OrderItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepo orderItemRepo;
    private final ItemRepo itemRepo;

    public Mono<OrderItem> findOrderItem(Long orderId, Long itemId) {
        return orderItemRepo.findByOrderIdAndItemId(orderId, itemId);
    }

    public Mono<Void> updateOrderItem(Order order, Item item, String actionName) {
        return findOrderItem(order.getId(), item.getId())
                .defaultIfEmpty(new OrderItem(item, order))
                .flatMap(orderItem -> {
                    switch (OrderAction.getActionByName(actionName)) {
                        case OrderAction.PLUS_ITEM -> {
                            return increaseQuantity(orderItem);
                        }
                        case OrderAction.MINUS_ITEM -> {
                            return decreaseQuantity(orderItem);
                        }
                        case OrderAction.DELETE_ITEM -> {
                            return deleteOrderItem(orderItem);
                        }
                        default -> {
                            return Mono.error(new IllegalArgumentException("Unknown action name: " + actionName));
                        }
                    }
                });
    }

    public Mono<Void> increaseQuantity(OrderItem orderItem) {
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        return orderItemRepo.save(orderItem).then();
    }

    public Mono<Void> decreaseQuantity(OrderItem orderItem) {
        int quantity = orderItem.getQuantity() == 0 ? 0 : orderItem.getQuantity() - 1;
        if (quantity == 0) {
            return deleteOrderItem(orderItem);
        } else {
            orderItem.setQuantity(quantity);
            return orderItemRepo.save(orderItem).then();
        }
    }

    public Mono<Void> deleteOrderItem(OrderItem orderItem) {
        return orderItemRepo.delete(orderItem);
    }

    public Mono<OrderResponse> getOrderResponseWithItems(Order order) {
        return orderItemRepo.findByOrderId(order.getId())
                .flatMap(orderItem ->
                        itemRepo.findById(orderItem.getItemId())
                                .map(item -> ItemResponse.builder()
                                        .id(item.getId())
                                        .title(item.getTitle())
                                        .description(item.getDescription())
                                        .imgPath(item.getImgPath())
                                        .count(orderItem.getQuantity())
                                        .price(item.getPrice())
                                        .build()
                                )
                )
                .collectList()
                .map(items -> {
                    BigDecimal totalSum = items.stream()
                            .map(ItemResponse::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new OrderResponse(order.getId(), items, totalSum);
                });
    }

    public Flux<OrderItem> getByOrderId(Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }
}
