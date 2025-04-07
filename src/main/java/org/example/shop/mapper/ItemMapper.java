package org.example.shop.mapper;

import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.Item;

public class ItemMapper {

    public static ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .description(item.getDescription())
                .imgPath(item.getImgPath())
                .count(0)
                .build();
    };
}
