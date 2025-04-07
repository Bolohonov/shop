package org.example.shop.api.response;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ItemResponse {
    private final Integer id;
    private final String title;
    private final BigDecimal price;
    private final String description;
    private final String imgPath;
    private int count = 0;
}
