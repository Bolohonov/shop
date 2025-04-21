package org.example.shop.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.api.response.OrderResponse;
import org.example.shop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Mono<Rendering> getOrders(WebSession session) {
        return Mono.just(
                Rendering.view("orders")
                        .modelAttribute("orders", orderService.getBySession(session.getId()))
                        .build()
        );
    }

    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable int orderId, Model model) {
        OrderResponse orderResponseDto = orderService.getById(orderId);
        model.addAttribute("order", orderResponseDto);
        return "order";
    }
}
