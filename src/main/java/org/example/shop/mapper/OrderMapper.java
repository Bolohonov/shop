package org.example.shop.mapper;

import org.example.shop.api.response.OrderResponse;
import org.example.shop.dto.OrderDto;
import org.example.shop.dto.OrderItemDto;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;

import java.math.BigDecimal;
import java.util.Set;

public class OrderMapper {

//    public static OrderDto toDto(Order order) {
//        return OrderDto.builder()
//                .id(order.getId())
//                .orderTime(order.getOrderTime())
//                .customer(order.getCustomer())
//                .session(order.getSession())
//                .status(order.getStatus())
//                .orderItems(order.getOrderItems().stream().map(OrderMapper::toDto).toList())
//                .build();
//    }
//
//    public static OrderItemDto toDto(OrderItem orderItem) {
//        return OrderItemDto.builder()
//                .itemId(orderItem.getItem().getId())
//                .quantity(orderItem.getQuantity())
//                .build();
//    }
//
//    public static OrderResponse toResponse(Order order) {
//        return OrderResponse.builder()
//                .id(order.getId())
//                .items(order.getOrderItems().stream().map(OrderItemMapper::toResponse).toList())
//                .totalSum(getTotalSum(order.getOrderItems()))
//                .build();
//    }
//
//    private static BigDecimal getTotalSum(Set<OrderItem> source) {
//        return source.stream()
//                .map(i -> i.getItem().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
}
