package com.example.shopapp.response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;

    private Long orderId;

    private Long productId;

    private Float price;

    private int numberOfProducts;

    private Float totalMoney;

    private String color;


}
