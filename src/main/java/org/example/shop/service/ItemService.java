package org.example.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.api.response.ItemResponse;
import org.example.shop.mapper.ItemMapper;
import org.example.shop.model.Item;
import org.example.shop.repo.ItemRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;
    private final ItemMapper itemMapper;
    private final OrderService orderService;

    public Flux<ItemResponse> getBySearchPageable(String search, String sortRaw, Integer pageSize, String session) {
        Sort sort = switch (sortRaw) {
            case "ALPHA" -> Sort.by("title");
            case "PRICE" -> Sort.by("price");
            default -> Sort.unsorted();
        };
        Pageable pageable = PageRequest.of(0, pageSize, sort);
        Flux<Item> items;
        if (StringUtils.hasLength(search)) {
            items = itemRepo.findByTitleContainsIgnoreCase(search, pageable);
        } else {
            items = itemRepo.findAllPageable(pageSize, 0);
        }
        return orderService.findOrderItemsMapBySession(session)
                .flatMapMany(
                        orderDto ->
                                items
                                        .map(itemMapper::toResponse)
                                        .map(item -> {
                                            item.setCount(orderDto.getOrDefault(item.getId(), 0));
                                            return item;
                                        })
                );
    }

    public Mono<ItemResponse> getById(Long itemId, String session) {
        return orderService.findOrderItemsMapBySession(session)
                .flatMap(
                        orderDto ->
                                itemRepo.findById(itemId)
                                        .map(itemMapper::toResponse)
                                        .map(item -> {
                                            item.setCount(orderDto.getOrDefault(item.getId(), 0));
                                            return item;
                                        })
                );
    }
}
