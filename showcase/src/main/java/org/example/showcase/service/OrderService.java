package org.example.showcase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.showcase.api.exception.OrderNotFoundException;
import org.example.showcase.api.exception.PaymentException;
import org.example.showcase.api.response.OrderResponse;
import org.example.showcase.api.response.OrderStatus;
import org.example.showcase.model.Item;
import org.example.showcase.model.Order;
import org.example.showcase.model.OrderItem;
import org.example.showcase.payment.api.PaymentApi;
import org.example.showcase.payment.model.PaymentRequestDto;
import org.example.showcase.repo.ItemRepo;
import org.example.showcase.repo.OrderRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final static String DEFAULT_CUSTOMER = "customer";

    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final OrderItemService orderItemService;
    private final PaymentApi paymentApi;

    public Mono<ResponseEntity<Void>> makeOrder(String sessionId, ServerWebExchange exchange) {
        return orderRepo.findOrderBySessionAndStatusContainsIgnoreCase(sessionId, OrderStatus.NEW.name())
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Заказ не найден")))
                .flatMap(order -> {
                    order.setStatus(OrderStatus.IN_PROGRESS.name());
                    return orderRepo.save(order);
                })
                .flatMap(orderItemService::getOrderResponseWithItems)
                .flatMap(order ->
                        processPayment(Long.valueOf(sessionId), order, exchange));
    }

    public Mono<Map<Long, Integer>> findOrderItemsMapBySession(String session) {
        return orderRepo.findOrderBySessionAndStatusContainsIgnoreCase(session, OrderStatus.NEW.name())
                .map(Order::getId)
                .flatMapMany(orderItemService::getByOrderId)
                .collectMap(OrderItem::getItemId, OrderItem::getQuantity);
    }

    public Mono<Void> updateOrder(Long itemId, String actionName, String sessionId) {
        return itemRepo.findById(itemId)
                .zipWith(getOrCreate(sessionId))
                .flatMap(tuple -> {
                    Item item = tuple.getT1();
                    Order order = tuple.getT2();

                    return orderItemService.updateOrderItem(order, item, actionName);
                    });

    }

    public Flux<OrderResponse> getBySession(String session) {
        return orderRepo.findBySessionAndStatusNotContainsIgnoreCase(session, OrderStatus.NEW.name())
                .flatMap(orderItemService::getOrderResponseWithItems)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Заказ не найден")));
    }

    public Mono<OrderResponse> getById(Long orderId) {
        return orderRepo.findById(orderId)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Заказ не найден")))
                .flatMap(orderItemService::getOrderResponseWithItems);
    }

    private Mono<Order> getOrCreate(String session) {
        return orderRepo
                .findOrderBySessionAndStatusContainsIgnoreCase(session, OrderStatus.NEW.name())
                .switchIfEmpty(createNew(session));
    }

    private Mono<Order> createNew(String session) {
            Order newOrder = new Order();
            newOrder.setSession(session);
            newOrder.setCustomer(DEFAULT_CUSTOMER);
            newOrder.setStatus(OrderStatus.NEW.name());
            return orderRepo.save(newOrder);
    }

    private Mono<ResponseEntity<Void>> processPayment(Long accountId, OrderResponse order, ServerWebExchange exchange) {
        double totalAmount = order.getTotalSum().doubleValue();

        return paymentApi.getBalance(accountId)
                .doOnNext(balanceDto -> log.info("Баланс = {}", balanceDto.getBalance()))
                .flatMap(balanceDto -> {
                    if (balanceDto.getBalance() < totalAmount) {
                        return Mono.error(new PaymentException("Недостаточно средств"));
                    }

                    PaymentRequestDto paymentRequest = new PaymentRequestDto().amount(totalAmount);

                    return paymentApi.makePayment(accountId, paymentRequest)
                            .then(Mono.defer(() -> {
                                String redirectUrl = "/orders/" + order.getId();
                                exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                                exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
                                return Mono.just(ResponseEntity.ok().build());
                            }));
                });
    }
}
