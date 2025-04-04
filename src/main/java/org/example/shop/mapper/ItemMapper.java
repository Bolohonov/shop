package org.example.shop.mapper;

import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.Item;
import org.example.shop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {

    @Mapping(target = "count", ignore = true)
    ItemResponse toResponse(Item source);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "title", source = "item.title")
    @Mapping(target = "price", source = "item.price")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "imgPath", source = "item.imgPath")
    @Mapping(target = "count", source = "quantity")
    ItemResponse toResponse(OrderItem source);
}
