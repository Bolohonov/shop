package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.api.response.OrderStatus;
import org.example.shop.dto.OrderDto;
import org.example.shop.dto.OrderItemDto;
import org.example.shop.repo.OrderRepo;
import org.example.shop.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;

    public Map<Integer, Integer> findOrderItemsMapBySession(String session) {
        return orderRepo.findBySessionAndStatus(session, OrderStatus.NEW.name())
                .map(orderMapper::toDto)
                .map(OrderDto::getOrderItems)
                .map(this::getOrderItemMap)
                .orElse(null);
    }

    private Map<Integer, Integer> getOrderItemMap(List<OrderItemDto> orderItemDto) {
        return orderItemDto.stream()
                .collect(
                        Collectors.toMap(
                                OrderItemDto::getItemId,
                                OrderItemDto::getQuantity
                        )
                );
    }
}
