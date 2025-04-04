package org.example.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class OrderDto {

    private final Long id;
    private final LocalDateTime orderTime;
    private final String customer;
    private final String session;
    private final String status;
    private final List<OrderItemDto> orderItems;
}
