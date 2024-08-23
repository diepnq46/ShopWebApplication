package com.example.shopapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    @Min(value = 1)
    private Long productId;

    @Size(min = 5, max = 200, message = "Image's name")
    private String imageUrl;
}
