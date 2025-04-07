package org.example.shop.api.controller;

import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getCart(Model model, HttpSession session) {
        Map<BigDecimal, List<ItemResponse>> result = cartService.getCart(session);
        if (!CollectionUtils.isEmpty(result)) {
            BigDecimal total = result.entrySet().stream().findFirst().get().getKey();
            model.addAttribute("items", result.get(total));
            model.addAttribute("total", total);
        }
        return "cart";
    }

    @PostMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addToCart(@PathVariable int itemId, OrderRequest request, HttpSession session) {
        orderService.updateOrder(itemId, request.action(), session.getId());
        return "redirect:/cart/items";
    }

    @PostMapping("/buy")
    public String makeOrder(RedirectAttributes redirectAttrs, HttpSession session) {
        Integer orderId = orderService.makeOrder(session.getId());
        if (orderId != null) {
            redirectAttrs.addFlashAttribute("newOrder", true);
            return "redirect:/orders/" + orderId;
        } else {
            return "redirect:/main/items";
        }
    }
}
