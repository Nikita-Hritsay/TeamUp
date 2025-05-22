package org.products.products.mapper;

import org.products.products.dto.CategoryRequestDto;
import org.products.products.dto.CategoryResponseDto;
import org.products.products.entity.Category;

public class CategoryMapper {

    public static CategoryResponseDto mapToCategoryResponseDto(Category category, CategoryResponseDto categoryResponseDto) {
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setDescription(category.getDescription());
        return categoryResponseDto;
    }

    public static Category mapCategoryRequestDtoToCategory(CategoryRequestDto categoryRequestDto, Category category) {
        category.setId(categoryRequestDto.getId());
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());
        return category;
    }

}
