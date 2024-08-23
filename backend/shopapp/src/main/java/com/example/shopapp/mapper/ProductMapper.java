package com.example.shopapp.mapper;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.entity.Product;
import com.example.shopapp.response.ProductResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    ProductDTO mapToProductDTO(Product product);

    Product mapToProduct(ProductDTO productDTO);

    @Mapping(source = "category.id", target = "categoryId")
    @BeanMapping(builder = @Builder( disableBuilder = true ))
    @SubclassMapping(source = Product.class, target = ProductResponse.class)
    ProductResponse mapToProductResponse(Product product);
}
