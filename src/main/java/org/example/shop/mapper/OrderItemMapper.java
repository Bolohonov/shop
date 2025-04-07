package org.example.shop.mapper;

import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.OrderItem;

import java.util.List;
import java.util.Set;

public class OrderItemMapper {

    public static ItemResponse toResponse(OrderItem orderItem) {
        System.out.println("toResponse");
        System.out.println(orderItem);
        System.out.println(orderItem.getItem());
        return ItemResponse.builder()
                .id(orderItem.getItem().getId())
                .title(orderItem.getItem().getTitle())
                .price(orderItem.getItem().getPrice())
                .description(orderItem.getItem().getDescription())
                .imgPath(orderItem.getItem().getImgPath())
                .count(orderItem.getQuantity())
                .build();
    }

    public static List<ItemResponse> toResponse(Set<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItemMapper::toResponse).toList();
    }
}
