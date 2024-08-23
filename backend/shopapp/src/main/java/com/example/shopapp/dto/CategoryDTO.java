package com.example.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot empty")
    private String name;
}
