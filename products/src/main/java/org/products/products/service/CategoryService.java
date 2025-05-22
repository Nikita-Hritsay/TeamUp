package org.products.products.service;

import lombok.AllArgsConstructor;
import org.products.products.dto.CategoryRequestDto;
import org.products.products.dto.CategoryResponseDto;
import org.products.products.entity.Category;
import org.products.products.exception.ItemAlreadyExistsException;
import org.products.products.exception.ResourceNotFoundException;
import org.products.products.mapper.CategoryMapper;
import org.products.products.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto fetchCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId.toString())
        );
        return CategoryMapper.mapToCategoryResponseDto(category, new CategoryResponseDto());
    }

    @Override
    public void createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = CategoryMapper.mapCategoryRequestDtoToCategory(categoryRequestDto, new Category());
        Optional<Category> byName = categoryRepository.findByName(category.getName());
        if (byName.isPresent())
            throw new ItemAlreadyExistsException(String.format("Category with name '%s' already exists", category.getName()));
        else
            categoryRepository.save(category);
    }

    @Override
    public boolean updateCategory(CategoryRequestDto categoryRequestDto) {
        boolean updated = false;
        Category category = CategoryMapper.mapCategoryRequestDtoToCategory(categoryRequestDto, new Category());
        Optional<Category> byName = categoryRepository.findByName(category.getName());
        if (byName.isPresent() && !Objects.equals(byName.get().getId(), category.getId()))
            throw new ItemAlreadyExistsException(String.format("Category with name '%s' already exists", category.getName()));
        else
            categoryRepository.save(category);
        updated = true;
        return updated;
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        boolean deleted = false;
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId.toString())
        );
        categoryRepository.delete(category);
        deleted = true;
        return deleted;
    }


}
