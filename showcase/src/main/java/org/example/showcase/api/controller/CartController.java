package org.example.showcase.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.showcase.api.request.OrderRequest;
import org.example.showcase.service.CartService;
import org.example.showcase.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
@Slf4j
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public Mono<Rendering> getCart(WebSession session) {
        return Mono.just(
                Rendering.view("cart")
                        .modelAttribute("items", cartService.getCartItems(session.getId()))
                        .modelAttribute("total", cartService.getCartTotalSum(session.getId()))
                        .build()
        );
    }

    @PostMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<String> addToCart(@PathVariable Long itemId, OrderRequest request, WebSession session) {
        return orderService.updateOrder(itemId, request.getAction(), session.getId()).thenReturn("redirect:/cart/items");
    }

    @PostMapping("/buy")
    public Mono<ResponseEntity<Void>> makeOrder(WebSession session, ServerWebExchange exchange) {
        return orderService.makeOrder(session.getId(), exchange);
    }
}
