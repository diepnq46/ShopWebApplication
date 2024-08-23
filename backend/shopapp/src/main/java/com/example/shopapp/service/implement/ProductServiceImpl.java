package com.example.shopapp.service.implement;

import com.example.shopapp.exception.InvalidException;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.entity.Category;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.ProductImage;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.mapper.ProductMapper;
import com.example.shopapp.repository.CategoryRepository;
import com.example.shopapp.repository.ProductImageRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.response.ProductResponse;
import com.example.shopapp.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDTO.getCategoryId()));

        // Convert DTO -> JPA Entity
        Product product = ProductMapper.MAPPER.mapToProduct(productDTO);

        product.setCategory(existingCategory);

        // Lưu vào db
        Product savedProduct = productRepository.save(product);

        // Trả về DTO
        return savedProduct;
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId,
                                                PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo page và limit
        return productRepository.searchProducts(categoryId, keyword, pageRequest)
                .map(ProductMapper.MAPPER::mapToProductResponse);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDTO.getCategoryId()));

        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setThumbnail(productDTO.getThumbnail());

        Product updatedProduct = productRepository.save(existingProduct);
        return updatedProduct;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws InvalidException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        ProductImage newProductImage = new ProductImage(null,
                existingProduct,
                productImageDTO.getImageUrl()
        );

        // Ko được insert quá 5 ảnh cho 1 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidException("Number of images must be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }


}
