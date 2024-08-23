package com.example.shopapp.response;

import com.example.shopapp.entity.ProductImage;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductResponse extends BaseResponse{
    private String name;

    private Float price;

    private String thumbnail;

    private String description;

    private Long categoryId;

    private List<ProductImage> productImages;
}
