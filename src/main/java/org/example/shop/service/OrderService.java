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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final static String DEFAULT_CUSTOMER = "customer";

    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final OrderItemService orderItemService;

    public Integer makeOrder(String sessionId) {
        Order order = orderRepo.findBySessionAndStatus(sessionId, OrderStatus.NEW.name()).orElse(null);
        if (order == null) return null;
        order.setStatus(OrderStatus.IN_PROGRESS.name());
        orderRepo.save(order);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public Map<Integer, Integer> findOrderItemsMapBySession(String session) {
        return orderRepo.findBySessionAndStatus(session, OrderStatus.NEW.name())
                .map(OrderMapper::toDto)
                .map(OrderDto::getOrderItems)
                .map(this::getOrderItemMap)
                .orElse(null);
    }

    @Transactional
    public void updateOrder(int itemId, String actionName, String sessionId) {
        Item item = itemRepo.getReferenceById(itemId);
        Order order = getOrCreate(sessionId);
        OrderAction action = OrderAction.getActionByName(actionName);
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(i -> i.getItem().getId().equals(item.getId()))
                .findFirst()
                .orElse(new OrderItem(item, order, 0));
        switch (action) {
            case OrderAction.PLUS_ITEM -> {
                orderItemService.increaseQuantity(order, orderItem);
            }
            case OrderAction.MINUS_ITEM -> {
                orderItemService.decreaseQuantity(order, orderItem);
            }
            case OrderAction.DELETE_ITEM -> {
                orderItemService.deleteOrderItem(order, orderItem);
            }
        }
        orderRepo.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getBySession(String session) {
        return orderRepo.findBySessionAndStatusNot(session, OrderStatus.NEW.name()).stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(int orderId) {
        return OrderMapper.toResponse(orderRepo.getReferenceById(orderId));
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getActualCart(String sessionId) {
        return orderRepo.findBySessionAndStatus(sessionId, OrderStatus.NEW.name())
                .map(Order::getOrderItems)
                .map(OrderItemMapper::toResponse)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(ItemResponse::getId))
                .toList();
    }


    private Order getOrCreate(String session) {
        Optional<Order> order = orderRepo.findBySessionAndStatus(session, OrderStatus.NEW.name());
        if (order.isPresent()) {
            return order.get();
        }
        Order newOrder = new Order();
        newOrder.setSession(session);
        newOrder.setCustomer(DEFAULT_CUSTOMER);
        newOrder.setStatus(OrderStatus.NEW.name());
        return orderRepo.save(newOrder);
    }

    private Map<Integer, Integer> getOrderItemMap(List<OrderItemDto> orderItemDto) {
        return orderItemDto.stream()
                .collect(Collectors.toMap(OrderItemDto::getItemId, OrderItemDto::getQuantity));
    }
}
