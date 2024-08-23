package com.example.shopapp.repository;

import com.example.shopapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm đơn hàng của 1 user
    List<Order> findByUserId(Long userId);
}
