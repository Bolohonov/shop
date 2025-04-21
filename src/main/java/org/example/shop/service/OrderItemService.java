package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.OrderItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {

    private final OrderItemRepo orderItemRepo;

    public Mono<OrderItem> findOrderItem(Integer orderId, Integer itemId) {
        return orderItemRepo.findByOrderIdAndItemId(orderId, itemId);
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
}
