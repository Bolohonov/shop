package org.example.shop.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.api.request.OrderRequest;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.service.CartService;
import org.example.shop.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
@Slf4j
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public Mono<Rendering> getCart(WebSession session) {
//        Map<BigDecimal, List<ItemResponse>> result = cartService.getCart(session);
//        if (!CollectionUtils.isEmpty(result)) {
//            BigDecimal total = result.entrySet().stream().findFirst().get().getKey();
//            model.addAttribute("items", result.get(total));
//            model.addAttribute("total", total);
//        }
    }

    @PostMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<String> addToCart(@PathVariable int itemId, OrderRequest request, WebSession session) {
        return orderService.updateOrder(itemId, request.action(), session.getId()).thenReturn("redirect:/cart/items");
    }

    @PostMapping("/buy")
    public Mono<Rendering> makeOrder(WebSession session) {
        return orderService.makeOrder(session.getId())
                .map(orderId ->
                        Rendering.view("redirect:/orders/" + orderId)
                                .modelAttribute("newOrder", true)
                                .build()
                );
    }
}
