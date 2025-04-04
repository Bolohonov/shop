package org.example.shop.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.service.ItemService;
import org.example.shop.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main/items")
@Slf4j
public class ShopController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping
    public String index(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "NO") String sort,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpSession session,
            Model model
    ) {
        Page<ItemResponse> itemDtos = itemService.getBySearchPageable(search, sort, pageSize, session.getId());
        model.addAttribute("sort", sort);
        model.addAttribute("items", itemDtos);
        return "main";
    }
}
