package com.example.shopapp.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
