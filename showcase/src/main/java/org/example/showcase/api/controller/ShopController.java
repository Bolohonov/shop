package org.example.showcase.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.showcase.api.request.OrderRequest;
import org.example.showcase.service.ItemService;
import org.example.showcase.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class ShopController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping
    public Mono<Rendering> index(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "NO") String sort,
            @RequestParam(defaultValue = "10") Integer pageSize,
            WebSession session
    ) {
        return Mono.just(
                Rendering.view("main")
                        .modelAttribute("sort", sort)
                        .modelAttribute("pageSize", pageSize)
                        .modelAttribute("items", itemService.getBySearchPageable(search, sort, pageSize, session.getId()))
                        .build()
        );
    }

    @PostMapping(value = "/main/items/{itemId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PreAuthorize("isAuthenticated()")
    public Mono<String> addToCart(@PathVariable Long itemId, OrderRequest request, WebSession session) {
        return orderService
                .updateOrder(itemId, request.getAction(), session.getId())
                .thenReturn("redirect:/");
    }
}
