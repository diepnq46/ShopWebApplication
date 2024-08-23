package com.example.shopapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Name must be less than or equal 200")
    private String name;

    @Min(value = 0, message = "Price must be greatee than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;

    private String thumbnail;

    private String description;

    private Long categoryId;

}
