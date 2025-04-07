package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.mapper.ItemMapper;
import org.example.shop.model.Item;
import org.example.shop.repo.ItemRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;
    private final OrderService orderService;

    public Page<ItemResponse> getBySearchPageable(String search, String sortRaw, Integer pageSize, String session) {
        Sort sort = switch (sortRaw) {
            case "ALPHA" -> Sort.by("title");
            case "PRICE" -> Sort.by("price");
            default -> Sort.unsorted();
        };
        Pageable pageable = PageRequest.of(0, pageSize, sort);
        Page<Item> items;
        if (StringUtils.hasLength(search)) {
            items = itemRepo.findByTitleContainsIgnoreCase(search, pageable);
        } else {
            items = itemRepo.findAll(pageable);
        }
        Page<ItemResponse> responses = items.map(ItemMapper::toResponse);
        Map<Integer, Integer> orderDto = orderService.findOrderItemsMapBySession(session);
        if (orderDto != null) {
            responses
                    .getContent()
                    .forEach(item -> item.setCount(orderDto.getOrDefault(item.getId(), 0)));
        }
        return responses;
    }

    public ItemResponse getById(int itemId, String session) {
        Item item = itemRepo.getReferenceById(itemId);
        ItemResponse response = ItemMapper.toResponse(item);
        Map<Integer, Integer> orderDto = orderService.findOrderItemsMapBySession(session);
        if (orderDto != null) {
            response.setCount(orderDto.get(response.getId()));
        }
        return response;
    }
}
