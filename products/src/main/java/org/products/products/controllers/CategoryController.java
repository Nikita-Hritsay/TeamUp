package org.products.products.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.products.products.constants.ProductsConstants;
import org.products.products.dto.CategoryRequestDto;
import org.products.products.dto.CategoryResponseDto;
import org.products.products.dto.ErrorResponseDto;
import org.products.products.dto.ResponseDto;
import org.products.products.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST API for Category",
        description = "CRUD REST API operations for Categories in Products microservice")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CategoryController {

    private ICategoryService categoryService;

    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @Operation(
            summary = "Create category REST API",
            description = "Create new Category REST API")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.createCategory(categoryRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ProductsConstants.STATUS_201, ProductsConstants.MESSAGE_201));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @Operation(
            summary = "Update category REST API",
            description = "Update new Category REST API")
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        boolean updated = categoryService.updateCategory(categoryRequestDto);
        if (updated)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(ProductsConstants.STATUS_200, ProductsConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(ProductsConstants.STATUS_500, ProductsConstants.MESSAGE_500));
    }

    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @Operation(
            summary = "Fetch category by id REST API",
            description = "Fetch Category by Id")
    @GetMapping("/fetch")
    public ResponseEntity<CategoryResponseDto> fetchCategory(
            @RequestParam Long id) {
        CategoryResponseDto category = categoryService.fetchCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @Operation(
            summary = "Delete category by Id REST API",
            description = "Delete Category REST API")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCategory(
            @RequestParam Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(ProductsConstants.STATUS_200, ProductsConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(ProductsConstants.STATUS_500, ProductsConstants.MESSAGE_500));
    }

}
