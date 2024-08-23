package com.example.shopapp.controller;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.entity.Category;
import com.example.shopapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@AllArgsConstructor
public class CategoryControlller {
    private final CategoryService categoryService;
    // Tạo mới 1 category
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        Category savedCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                      @PathVariable("id") Long id){
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryDTO), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id: " + id + " successfully");
    }
}
