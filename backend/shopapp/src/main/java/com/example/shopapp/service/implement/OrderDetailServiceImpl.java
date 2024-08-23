package com.example.shopapp.service.implement;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.entity.Order;
import com.example.shopapp.entity.OrderDetail;
import com.example.shopapp.entity.Product;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.mapper.OrderDetailMapper;
import com.example.shopapp.repository.OrderDetailRepository;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.response.OrderDetailResponse;
import com.example.shopapp.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", orderDetailDTO.getProductId()));

        // Convert DTO qua JPA Entity
        OrderDetail orderDetail = OrderDetailMapper.MAPPER.mapToOrderDetail(orderDetailDTO);
        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);

        // Lưu vào db
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return OrderDetailMapper.MAPPER.mapToOrderDetailResponse(savedOrderDetail);
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", "id", id));

        return OrderDetailMapper.MAPPER.mapToOrderDetailResponse(orderDetail);
    }

    @Override
    @Transactional
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", orderDetailDTO.getProductId()));

        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", "id", id));

        orderDetail.setOrder(existingOrder);
        orderDetail.setProduct(existingProduct);
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setColor(orderDetailDTO.getColor());

        OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);
        return OrderDetailMapper.MAPPER.mapToOrderDetailResponse(updatedOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", "id", id));
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetails(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(OrderDetailMapper.MAPPER::mapToOrderDetailResponse)
                .toList();
    }
}
