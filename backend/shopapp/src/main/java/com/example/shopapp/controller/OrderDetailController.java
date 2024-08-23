package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.response.OrderDetailResponse;
import com.example.shopapp.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@AllArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO){
        return new ResponseEntity<>(
                orderDetailService.createOrderDetail(orderDetailDTO),
                HttpStatus.CREATED
        );
    }

    // Get All OrderDetails By orderId
    @GetMapping("order/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetails(@PathVariable("orderId") Long orderId){
        return ResponseEntity.ok(
                orderDetailService.getOrderDetails(orderId)
        );
    }

    // Get OrderDetail By Id
    @GetMapping("{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                orderDetailService.getOrderDetail(id)
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderDetailResponse> updateOrderDetail(@PathVariable("id") Long id,
                                                         @RequestBody @Valid OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok(
                orderDetailService.updateOrderDetail(id, orderDetailDTO)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable("id") Long id){
        orderDetailService.deleteOrderDetail(id);

        return ResponseEntity.ok("Deleted order detail successfully with id: " + id);
    }
}
