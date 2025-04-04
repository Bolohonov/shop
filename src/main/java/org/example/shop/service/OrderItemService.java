package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.OrderItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {

    private final OrderItemRepo orderItemRepo;

    public void increaseQuantity(Order order, OrderItem orderItem) {
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        order.getOrderItems().add(orderItem);
    }

    public void decreaseQuantity(Order order, OrderItem orderItem) {
        int quantity = orderItem.getQuantity() == 0 ? 0 : orderItem.getQuantity() - 1;
        if (quantity == 0) {
            deleteOrderItem(order, orderItem);
        } else {
            orderItem.setQuantity(quantity);
            order.getOrderItems().add(orderItem);
        }
    }

    public void deleteOrderItem(Order order, OrderItem orderItem) {
        order.getOrderItems().remove(orderItem);
        orderItemRepo.delete(orderItem);
    }
}
