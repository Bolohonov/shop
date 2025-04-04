package org.example.shop.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.shop.api.response.ItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderService orderService;

    public Map<BigDecimal, List<ItemResponse>> getCart(HttpSession session) {
        Map<BigDecimal, List<ItemResponse>> result = new HashMap<>();
        List<ItemResponse> items = orderService.getActualCart(session.getId());
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put(total, items);
        return result;
    }
}
