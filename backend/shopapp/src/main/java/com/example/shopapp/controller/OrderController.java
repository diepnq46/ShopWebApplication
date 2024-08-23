package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.entity.Order;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderDTO orderDTO){
        return new ResponseEntity<>(orderService.createOrder(orderDTO),
                HttpStatus.CREATED);
    }


    // Lấy ra tất cả đơn hàng của người dùng
    @GetMapping("user/{user_id}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable("user_id") Long userId){
        return new ResponseEntity<>(
                orderService.findByUserId(userId),
                HttpStatus.OK
        );
    }

    // Lấy ra 1 đơn hàng theo Id
    @GetMapping("{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id){
        return new ResponseEntity<>(
                orderService.getOrder(id),
                HttpStatus.OK
        );
    }

    // Update Order
    @PutMapping("{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody @Valid OrderDTO orderDTO,
                                                     @PathVariable("id") Long id){
        return new ResponseEntity<>(
                orderService.updateOrder(id, orderDTO),
                HttpStatus.OK
        );
    }

    // Delete Order
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Deleted sucessfully with id: " + id);
    }
}
