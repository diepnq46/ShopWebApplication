package com.example.shopapp.service;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO);

    OrderDetailResponse getOrderDetail(Long id);

    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);

    void deleteOrderDetail(Long id);

    List<OrderDetailResponse> getOrderDetails(Long orderId);
}
