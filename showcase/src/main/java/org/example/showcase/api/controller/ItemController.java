package org.example.showcase.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.showcase.api.request.OrderRequest;
import org.example.showcase.service.ItemService;
import org.example.showcase.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    @PostMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<String> addToCart(@PathVariable Long itemId, OrderRequest request, WebSession session) {
        return orderService.updateOrder(itemId, request.getAction(), session.getId())
                .thenReturn("redirect:/items/{itemId}");
    }

    @GetMapping("/{itemId}")
    public Mono<Rendering> getItem(@PathVariable Long itemId, WebSession session) {
        return Mono.just(
                Rendering.view("item")
                        .modelAttribute("item", itemService.getById(itemId, session.getId()))
                        .build()
        );
    }
}
