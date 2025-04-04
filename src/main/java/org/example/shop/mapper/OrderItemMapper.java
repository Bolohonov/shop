package org.example.shop.mapper;

import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper
public interface OrderItemMapper {

    @Mapping(target = "id", source = "item.id" )
    @Mapping(target = "title", source = "item.title" )
    @Mapping(target = "price", source = "item.price" )
    @Mapping(target = "description", source = "item.description" )
    @Mapping(target = "imgPath", source = "item.imgPath" )
    @Mapping(target = "count", source = "quantity" )
    ItemResponse toResponse(OrderItem source);

    List<ItemResponse> toResponse(Set<OrderItem> source);
}
