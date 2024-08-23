package com.example.shopapp.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse{
    private Long id;

    private Long userId;

    private String fullname;

    private String phoneNumber;

    private String address;

    private String note;

    private LocalDateTime orderDate;

    private String status;

    private Float totalMoney;

    private String shippingMethod;

    private String trackingNumber;

    private String paymentMethod;

    private Boolean active;
}
