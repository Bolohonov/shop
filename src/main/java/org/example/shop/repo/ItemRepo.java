package org.example.shop.repo;

import org.example.shop.model.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemRepo extends R2dbcRepository<Item, Integer> {

    Flux<Item> findByTitleContainsIgnoreCase(String title, Pageable pageable);
}
