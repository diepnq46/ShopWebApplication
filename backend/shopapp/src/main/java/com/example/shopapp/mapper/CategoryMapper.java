package com.example.shopapp.mapper;

import com.example.shopapp.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.example.shopapp.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO mapToCategoryDTO(Category category);

    Category mapToCategory(CategoryDTO categoryDTO);
}
