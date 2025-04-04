package org.example.shop.repo;

import org.example.shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

    Optional<Order> findBySessionAndStatus(String session, String status);

    List<Order> findBySessionAndStatusNot(String session, String status);
}
