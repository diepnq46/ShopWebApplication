package com.example.shopapp.controller;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.entity.Product;
import com.example.shopapp.entity.ProductImage;
import com.example.shopapp.response.ProductListResponse;
import com.example.shopapp.response.ProductResponse;
import com.example.shopapp.service.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;
    // Get all products
    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit){
        PageRequest pageRequest = PageRequest.of(page-1, limit,
//                Sort.by("createdAt").descending());
                    Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(
                keyword,
                categoryId,
                pageRequest);

        // Lấy tỏng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    // Get product
    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Create product
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO
                                          // @RequestPart MultipartFile file,){
    ){
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
                                          @RequestBody List<MultipartFile> files){
        // Tạo mảng rỗng nếu files bị null (tránh lỗi vòng lặp)
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    // Kích thước ko quá 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }

                String filename = storeFile(file);

                // Lưu vào bảng product_images
                ProductImage productImage = productService.createProductImage(existingProduct.getId(), new ProductImageDTO(
                        null,
                        filename
                ));
                productImages.add(productImage);
            }

            return new ResponseEntity<>(productImages, HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        // Thêm UUID trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục muốn lưu file
        Path uploatDir = Paths.get("uploads");

        if (!Files.exists(uploatDir)){
            Files.createDirectories(uploatDir);
        }

        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploatDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable("imageName") String imageName){
        try{
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if(resource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    // Delete product
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete sucessfully with id: " + id);
    }

    // Update product
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id,
                                                 @RequestBody @Valid ProductDTO productDTO){
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

//    @PostMapping("generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i < 1_000_000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(1,  3))
                    .build();
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }

    @GetMapping("/by-ids")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestParam("ids") String ids){
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

        return ResponseEntity.ok(productService
                .findProductsByIds(productIds));
    }
}
