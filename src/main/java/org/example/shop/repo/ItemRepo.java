package org.example.shop.repo;

import org.example.shop.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer> {

    Page<Item> findByTitleContainsIgnoreCase(String title, Pageable pageable);
}
