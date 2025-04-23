package org.example.shop.mapper;

import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {
    @Mapping(target = "count", ignore = true)
    ItemResponse toResponse(Item source);
}