package com.example.shopapp.service;

import com.example.shopapp.exception.InvalidException;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.ProductImage;
import com.example.shopapp.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);

    Product getProductById(long id);

    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId,
                                         PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO);

    void deleteProduct(long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws InvalidException;

    List<Product> findProductsByIds(List<Long> productIds);
}
