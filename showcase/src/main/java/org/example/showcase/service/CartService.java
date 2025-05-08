package org.example.showcase.service;

import lombok.RequiredArgsConstructor;
import org.example.showcase.api.exception.CartNotFoundException;
import org.example.showcase.api.response.ItemResponse;
import org.example.showcase.api.response.OrderStatus;
import org.example.showcase.model.Order;
import org.example.showcase.repo.ItemRepo;
import org.example.showcase.repo.OrderItemRepo;
import org.example.showcase.repo.OrderRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ItemRepo itemRepo;
    private final OrderService orderService;

    public Flux<ItemResponse> getCartItems(String session) {
        return orderRepo
                .findOrderBySessionAndStatusContainsIgnoreCase(session, OrderStatus.NEW.name())
                .map(Order::getId)
                .switchIfEmpty(Mono.error(new CartNotFoundException("Корзина не найдена")))
                .flatMapMany(orderItemRepo::findByOrderId)
                .flatMap(orderItem -> itemRepo.findById(orderItem.getItemId()).map(item ->
                                ItemResponse.builder()
                                        .id(orderItem.getItemId())
                                        .title(item.getTitle())
                                        .price(item.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                                        .description(item.getDescription())
                                        .imgPath(item.getImgPath())
                                        .count(orderItem.getQuantity())
                                        .build()
                        )
                )
                .sort(Comparator.comparing(ItemResponse::getId));
    }

    public Mono<BigDecimal> getCartTotalSum(String session) {
        return getCartItems(session)
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getCount())))
                .switchIfEmpty(Mono.error(new CartNotFoundException("Корзина не найдена")))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
