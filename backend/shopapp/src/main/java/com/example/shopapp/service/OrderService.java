package com.example.shopapp.service;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.entity.Order;
import com.example.shopapp.response.OrderResponse;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);

    List<Order> findByUserId(Long userId);

    Order getOrder(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO);

    void deleteOrder(Long id);
}
