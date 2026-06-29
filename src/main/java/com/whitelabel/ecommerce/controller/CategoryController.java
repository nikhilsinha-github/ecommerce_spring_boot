package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ApiResponse;
import com.whitelabel.ecommerce.dto.CategoryRequest;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
        if(title != null && !title.isEmpty()) {
            Optional<Page<Category>> optionalCategory = categoryService.getCategoryByTitle(pageNumber, size, title);

            if(optionalCategory.isEmpty()) {
                throw new RuntimeException("Category with title: "+ title + " not found");
            }

            return ResponseEntity.ok(ApiResponse.success(200, "All categories are retrieved successfully", optionalCategory.get()));
        }

        return ResponseEntity.ok(ApiResponse.success(200, "All categories retrieved successfully", categoryService.getAllCategories(pageNumber, size)));
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(id);

        if(optionalCategory.isEmpty()) {
            throw new RuntimeException("Category with id: "+id+" not found");
        }

        return ResponseEntity.ok(ApiResponse.success(200, "Category retrieved successfully", optionalCategory.get()));
    }

    @Operation(summary = "Create category")
    @PostMapping
    ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(200, "Product created successfully", categoryService.createCategory(request)));
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody String title, @RequestBody String description) {
        return ResponseEntity.ok(ApiResponse.success(200,"Product updated successfully", categoryService.updateCategory(id, title, description)));
    }

    @Operation(summary = "Delete category by Id")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
