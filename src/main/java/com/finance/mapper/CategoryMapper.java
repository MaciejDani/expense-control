package com.finance.mapper;

import com.finance.dto.CategoryDto;
import com.finance.model.Category;

public class CategoryMapper {

    public static CategoryDto toDTO(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public static Category fromDTO(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return category;
    }
}
