package org.example.shop.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.api.request.OrderRequest;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.service.ItemService;
import org.example.shop.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    @PostMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addToCart(@PathVariable int itemId, OrderRequest request, HttpSession session) {
        orderService.updateOrder(itemId, request.action(), session.getId());
        return "redirect:/api/items/{itemId}";
    }

    @GetMapping("/{itemId}")
    public String getItem(@PathVariable int itemId, Model model, HttpSession session) {
        ItemResponse itemResponse = itemService.getById(itemId, session.getId());
        model.addAttribute("item", itemResponse);
        return "item";
    }
}
