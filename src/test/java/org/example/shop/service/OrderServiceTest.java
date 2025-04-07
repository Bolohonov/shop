package org.example.shop.service;

import org.example.shop.api.request.OrderAction;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.api.response.OrderResponse;
import org.example.shop.api.response.OrderStatus;
import org.example.shop.model.Item;
import org.example.shop.model.Order;
import org.example.shop.model.OrderItem;
import org.example.shop.repo.ItemRepo;
import org.example.shop.repo.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderService orderService;

    private final String TEST_SESSION_ID = "test-session";
    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testOrder = new Order();
        testOrder.setId(1);
        testOrder.setSession(TEST_SESSION_ID);
        testOrder.setStatus(OrderStatus.NEW.name());
        testOrder.setCustomer("customer");

        OrderItem testOrderItem = new OrderItem();
        testOrderItem.setItem(getItem()); // Используйте ваш класс Item
        testOrderItem.setQuantity(2);
        testOrder.setOrderItems(Collections.singleton(testOrderItem));
    }

    @Test
    void testMakeOrder_Success() {
        when(orderRepo.findBySessionAndStatus(TEST_SESSION_ID, OrderStatus.NEW.name())).thenReturn(Optional.of(testOrder));

        Integer result = orderService.makeOrder(TEST_SESSION_ID);

        assertNotNull(result);
        assertEquals(1, result);
        assertEquals(OrderStatus.IN_PROGRESS.name(), testOrder.getStatus());
        verify(orderRepo).save(testOrder);
    }

    @Test
    void testMakeOrder_NullOrder() {
        when(orderRepo.findBySessionAndStatus(TEST_SESSION_ID, OrderStatus.NEW.name())).thenReturn(Optional.empty());

        Integer result = orderService.makeOrder(TEST_SESSION_ID);

        assertNull(result);
    }

    @Test
    void testFindOrderItemsMapBySession_Success() {
        when(orderRepo.findBySessionAndStatus(TEST_SESSION_ID, OrderStatus.NEW.name()))
                .thenReturn(Optional.of(testOrder));

        Map<Integer, Integer> result = orderService.findOrderItemsMapBySession(TEST_SESSION_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(1));
    }

    @Test
    void testFindOrderItemsMapBySession_NullOrder() {
        when(orderRepo.findBySessionAndStatus(TEST_SESSION_ID, OrderStatus.NEW.name())).thenReturn(Optional.empty());

        Map<Integer, Integer> result = orderService.findOrderItemsMapBySession(TEST_SESSION_ID);

        assertNull(result);
    }

    @Test
    void testGetBySession_Success() {
        when(orderRepo.findBySessionAndStatusNot(TEST_SESSION_ID, OrderStatus.NEW.name()))
                .thenReturn(Collections.singletonList(testOrder));

        List<OrderResponse> responses = orderService.getBySession(TEST_SESSION_ID);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    void testGetBySession_NoOrders() {
        when(orderRepo.findBySessionAndStatusNot(TEST_SESSION_ID, OrderStatus.NEW.name()))
                .thenReturn(Collections.emptyList());

        List<OrderResponse> responses = orderService.getBySession(TEST_SESSION_ID);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testGetActualCart_Success() {
        when(orderRepo.findBySessionAndStatus(TEST_SESSION_ID, OrderStatus.NEW.name()))
                .thenReturn(Optional.of(testOrder));

        List<ItemResponse> itemResponses = orderService.getActualCart(TEST_SESSION_ID);

        assertNotNull(itemResponses);
        assertEquals(1, itemResponses.size());
        assertEquals(1, itemResponses.get(0).getId());
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1);
        item.setDescription("description");
        item.setTitle("Test Item");
        item.setPrice(BigDecimal.valueOf(100.00));
        item.setImgPath("no image");

        return item;
    }
}
