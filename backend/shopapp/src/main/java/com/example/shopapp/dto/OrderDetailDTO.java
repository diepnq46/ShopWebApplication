package com.example.shopapp.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private Long orderId;

    private Long productId;

    @Min(value = 0, message = "Price is greater than or equals to 1")
    private Float price;

    @Min(value = 1, message = "Number of products is greater than or equals to 1")
    private int numberOfProducts;

    @Min(value = 0, message = "Total money is greater than or equals to 1")
    private Float totalMoney;
    private String color;
}
