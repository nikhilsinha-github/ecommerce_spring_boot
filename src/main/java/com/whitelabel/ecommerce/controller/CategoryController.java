package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ApiResponse;
import com.whitelabel.ecommerce.dto.CategoryRequest;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "Categories", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    ResponseEntity<ApiResponse<Page<Category>>> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
    ) {
        Page<Category> categories = (title != null && !title.isEmpty())
                ? categoryService.getCategoryByTitle(pageNumber, size, title)
                : categoryService.getAllCategories(pageNumber, size);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "All categories are retrieved successfully",
                        categories
                )
        );
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Category retrieved successfully",
                        category
                )
        );
    }

    @Operation(summary = "Create category")
    @PostMapping
    ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        201,
                        "Product created successfully",
                        category
                )
        );
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        Category updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Product updated successfully",
                        updatedCategory
                )
        );
    }

    @Operation(summary = "Delete category by Id")
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Category deleted successfully",
                        null
                )
        );
    }
}
