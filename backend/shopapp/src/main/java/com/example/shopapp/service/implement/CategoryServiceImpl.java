package com.example.shopapp.service.implement;

import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.entity.Category;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.mapper.CategoryMapper;
import com.example.shopapp.repository.CategoryRepository;
import com.example.shopapp.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        // chuyển DTO sang entity JPA
        Category category = CategoryMapper.MAPPER.mapToCategory(categoryDTO);

        // Lưu vào db


        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.deleteById(id);
    }
}
