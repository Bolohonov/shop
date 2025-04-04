package org.example.shop.mapper;

import org.example.shop.api.response.OrderResponse;
import org.example.shop.dto.OrderDto;
import org.example.shop.dto.OrderItemDto;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Set;

@Mapper(uses = {ItemMapper.class})
public interface OrderMapper {

    OrderDto toDto(Order source);

    @Mapping(target = "itemId", source = "item.id")
    OrderItemDto toDto(OrderItem source);

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "totalSum", source = "orderItems", qualifiedByName = "getTotalSum")
    OrderResponse toResponse(Order source);

    @Named("getTotalSum")
    default BigDecimal getTotalSum(Set<OrderItem> source) {
        return source.stream()
                .map(i -> i.getItem().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
