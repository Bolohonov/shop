package org.example.showcase.api.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ItemResponse {
    private final Long id;
    private final String title;
    private final BigDecimal price;
    private final String description;
    private final String imgPath;
    private int count = 0;
}
