package org.example.shop.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.api.response.OrderResponse;
import org.example.shop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String getOrders(Model model, HttpSession session) {
        List<OrderResponse> orders = orderService.getBySession(session.getId());
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{orderId}")
    public String getOrder(@PathVariable int orderId, Model model) {
        OrderResponse orderResponseDto = orderService.getById(orderId);
        model.addAttribute("order", orderResponseDto);
        return "order";
    }
}
