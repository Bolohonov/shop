package org.example.shop.service;

import jakarta.servlet.http.HttpSession;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartService cartService;

    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = Mockito.mock(HttpSession.class);
    }

    @Test
    void testGetCart_EmptyCart() {
        // Arrange
        when(session.getId()).thenReturn("sessionId");
        when(orderService.getActualCart("sessionId")).thenReturn(Collections.emptyList());

        // Act
        Map<BigDecimal, List<ItemResponse>> result = cartService.getCart(session);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetCart_NonEmptyCart() {
        // Arrange
        when(session.getId()).thenReturn("sessionId");

        ItemResponse item1 = getItemResponse(10, 2); // Total: 20
        ItemResponse item2 = getItemResponse(15, 1); // Total: 15
        List<ItemResponse> items = List.of(item1, item2);
        when(orderService.getActualCart("sessionId")).thenReturn(items);

        Map<BigDecimal, List<ItemResponse>> result = cartService.getCart(session);

        BigDecimal expectedTotal = BigDecimal.valueOf(35); // 20 + 15
        assertEquals(1, result.size());
        assertEquals(expectedTotal, result.keySet().iterator().next());
        assertEquals(items, result.get(expectedTotal));
    }

    private ItemResponse getItemResponse(int price, int count) {
        return ItemResponse.builder()
                .id(1)
                .description("description")
                .title("title")
                .price(BigDecimal.valueOf(price))
                .imgPath("imgPath")
                .count(count)
                .build();
    }
}
