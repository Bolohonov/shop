package org.example.shop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.shop.api.response.ItemResponse;
import org.example.shop.model.Item;
import org.example.shop.repo.ItemRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private static final int DEFAULT_COUNT = 0;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetBySearchPageable_WithSearch() {
        String search = "test";
        String sortRaw = "ALPHA";
        int pageSize = 10;
        String session = "session123";

        Item item1 = getItem(1, 100);
        Item item2 = getItem(2, 150);
        List<Item> itemList = List.of(item1, item2);
        Page<Item> itemPage = new PageImpl<>(itemList);
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by("title"));

        when(itemRepo.findByTitleContainsIgnoreCase(search, pageable)).thenReturn(itemPage);

        Map<Integer, Integer> orderMap = new HashMap<>();
        orderMap.put(item1.getId(), 1);
        orderMap.put(item2.getId(), 2);
        when(orderService.findOrderItemsMapBySession(session)).thenReturn(orderMap);

        Page<ItemResponse> result = itemService.getBySearchPageable(search, sortRaw, pageSize, session);

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().get(0).getCount());
        assertEquals(2, result.getContent().get(1).getCount());
    }

    @Test
    public void testGetBySearchPageable_WithoutSearch() {
        String search = "";
        int pageSize = 10;
        String session = "session123";

        Item item1 = getItem(1, 100);
        List<Item> itemList = List.of(item1);
        Page<Item> itemPage = new PageImpl<>(itemList);

        when(itemRepo.findAll(PageRequest.of(0, pageSize, Sort.unsorted()))).thenReturn(itemPage);

        when(orderService.findOrderItemsMapBySession(session)).thenReturn(null);

        Page<ItemResponse> result = itemService.getBySearchPageable(search, "", pageSize, session);

        assertEquals(1, result.getTotalElements());
        assertEquals(DEFAULT_COUNT, result.getContent().get(0).getCount());
    }

    @Test
    public void testGetById_WithOrder() {
        int itemId = 1;
        String session = "session123";

        Item item = getItem(itemId, 100);
        when(itemRepo.getReferenceById(itemId)).thenReturn(item);

        Map<Integer, Integer> orderMap = new HashMap<>();
        orderMap.put(itemId, 3);
        when(orderService.findOrderItemsMapBySession(session)).thenReturn(orderMap);

        ItemResponse result = itemService.getById(itemId, session);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(3, result.getCount());
    }

    @Test
    public void testGetById_WithoutOrder() {
        int itemId = 1;
        String session = "session123";

        Item item = getItem(itemId, 100);
        when(itemRepo.getReferenceById(itemId)).thenReturn(item);

        when(orderService.findOrderItemsMapBySession(session)).thenReturn(null);

        ItemResponse result = itemService.getById(itemId, session);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(DEFAULT_COUNT, result.getCount());
    }

    private Item getItem(int id, int price) {
        Item item = new Item();
        item.setId(id);
        item.setDescription("description");
        item.setTitle("Test Item");
        item.setPrice(BigDecimal.valueOf(price));
        item.setImgPath("no image");

        return item;
    }
}
