package org.products.products.service;

import org.products.products.dto.CategoryRequestDto;
import org.products.products.dto.CategoryResponseDto;

public interface ICategoryService {

    public CategoryResponseDto fetchCategory(Long categoryId);

    void createCategory(CategoryRequestDto categoryRequestDto);

    boolean updateCategory(CategoryRequestDto categoryRequestDto);

    boolean deleteCategory(Long categoryId);

}
