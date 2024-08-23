package com.example.shopapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long userId;

    private String fullname;

    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    private String note;

    @Min(value = 0, message = "Total money is greater than or equals to 0")
    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private String paymentMethod;
    private LocalDate shippingDate;

    private List<CartItemDTO> cartItems;
}
