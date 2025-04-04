package org.example.shop.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemResponse {
    private final Integer id;
    private final String title;
    private final BigDecimal price;
    private final String description;
    private final String imgPath;
    private int count = 0;
}
