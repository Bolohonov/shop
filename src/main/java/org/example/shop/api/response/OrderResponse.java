package org.example.shop.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderResponse {
    private final Long id;
    private final List<ItemResponse> items;
    private final BigDecimal totalSum;
}
