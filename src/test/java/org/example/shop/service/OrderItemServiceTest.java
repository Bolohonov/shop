package org.example.shop.service;

import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.OrderItemRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepo orderItemRepo;

    @InjectMocks
    private OrderItemService orderItemService;

    @Test
    void testIncreaseQuantity() {
        // Arrange
        Order order = new Order();
        order.setId(1);
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        Set<OrderItem> orderItems = new LinkedHashSet<>();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        orderItemService.increaseQuantity(order, orderItem);

        // Assert
        assertEquals(2, orderItem.getQuantity());
        assertTrue(order.getOrderItems().contains(orderItem));
    }

    @Test
    void testDecreaseQuantity() {
        // Arrange
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        Set<OrderItem> orderItems = new LinkedHashSet<>();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        orderItemService.decreaseQuantity(order, orderItem);

        assertFalse(order.getOrderItems().contains(orderItem));
        verify(orderItemRepo).delete(orderItem); // Проверка, что orderItem был удален
    }

    @Test
    void testDecreaseQuantityPreservesItemWhenNotZero() {
        // Arrange
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        Set<OrderItem> orderItems = new LinkedHashSet<>();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        orderItemService.decreaseQuantity(order, orderItem);

        assertEquals(1, orderItem.getQuantity());
        assertTrue(order.getOrderItems().contains(orderItem));
        verify(orderItemRepo, never()).delete(orderItem); // Проверка, что delete не был вызван
    }

    @Test
    void testDeleteOrderItem() {
        // Arrange
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        Set<OrderItem> orderItems = new LinkedHashSet<>();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);

        orderItemService.deleteOrderItem(order, orderItem);

        assertFalse(order.getOrderItems().contains(orderItem));
        verify(orderItemRepo).delete(orderItem); // Проверка, что orderItem был удален
    }
}
