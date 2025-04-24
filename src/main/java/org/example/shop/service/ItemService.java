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
    private final OrderService orderService;

    public Flux<ItemResponse> getBySearchPageable(String search, String sortRaw, Integer pageSize, String session) {
        Sort sort = switch (sortRaw) {
            case "ALPHA" -> Sort.by("title");
            case "PRICE" -> Sort.by("price");
            default -> Sort.unsorted();
        };
        Pageable pageable = PageRequest.of(0, pageSize, sort);
        if (StringUtils.hasLength(search)) {
            return itemRepo.findByTitleContainsIgnoreCase(search, pageable).map(ItemMapper::toResponse);
        } else {
            return itemRepo.findAllPageable(pageSize, 0).map(ItemMapper::toResponse);
        }
    }

    public Mono<ItemResponse> getById(Long itemId, String session) {
        return orderService.findOrderItemsMapBySession(session)
                .flatMap(
                        orderDto ->
                                itemRepo.findById(itemId)
                                        .map(ItemMapper::toResponse)
                                        .map(item -> {
                                            item.setCount(orderDto.getOrDefault(item.getId(), 0));
                                            return item;
                                        })
                );
    }
}
