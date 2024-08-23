package com.example.shopapp.service.implement;

import com.example.shopapp.dto.CartItemDTO;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.entity.*;
import com.example.shopapp.exception.InvalidException;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.mapper.OrderMapper;
import com.example.shopapp.repository.OrderDetailRepository;
import com.example.shopapp.repository.OrderRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.response.OrderDetailResponse;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", orderDTO.getUserId()));

        // Convert DTO sang JPA Entity
        Order order = OrderMapper.MAPPER.mapToOrder(orderDTO);

        order.setUser(existingUser);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENNDING);

        // Kiểm tra shipping date phải lớn hơn hoặc bằng thời điểm đặt hàng
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new InvalidException("Date must be least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);

        // Lưu vào db
        Order savedOrder = orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for(CartItemDTO cartItem : orderDTO.getCartItems()){
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItem.getProductId()));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProduct(product);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setNumberOfProducts(cartItem.getQuantity());
            orderDetails.add(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);

        // Trả về response
        return savedOrder;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        User exsitingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", orderDTO.getUserId()));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        Order updateOrder = OrderMapper.MAPPER.mapToOrder(orderDTO);

        updateOrder.setId(order.getId());
        updateOrder.setUser(exsitingUser);
        updateOrder.setActive(order.getActive());

        return orderRepository.save(updateOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        order.setActive(false);
        orderRepository.save(order);
    }
}
