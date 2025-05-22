package org.products.products.mapper;

import org.products.products.dto.CategoryResponseDto;
import org.products.products.dto.ProductResponseDto;
import org.products.products.entity.Product;

public class ProductsMapper {

    public static ProductResponseDto mapToProductResponseDto(Product product, ProductResponseDto productResponseDto) {
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setCategory(CategoryMapper.mapToCategoryResponseDto(product.getCategory(), new CategoryResponseDto()));
        return productResponseDto;
    }

}
