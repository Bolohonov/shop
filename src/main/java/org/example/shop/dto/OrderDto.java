package org.example.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderDto {

    private final Integer id;
    private final LocalDateTime orderTime;
    private final String customer;
    private final String session;
    private final String status;
    private final List<OrderItemDto> orderItems;
}
