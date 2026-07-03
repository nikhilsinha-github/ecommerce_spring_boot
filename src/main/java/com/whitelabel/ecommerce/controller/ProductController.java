package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ApiResponse;
import com.whitelabel.ecommerce.dto.ProductRequest;
import com.whitelabel.ecommerce.dto.ProductResponse;
import com.whitelabel.ecommerce.entity.Product;
import com.whitelabel.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name="Products", description = "APIs for managing products")
public class ProductController {

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private final ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping
    ApiResponse<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
    ) {
        if(title != null && !title.isEmpty()) {
            Page<ProductResponse> products = productService.getProductByTitle(pageNumber, size, title);
            return ApiResponse.success(200, "Products retrieved successfully", products);
        }
        return ApiResponse.success(200, "Products retrieved successfully", productService.getAllProducts(pageNumber, size));
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        Optional<ProductResponse> optionalProduct = productService.getProductById(id);

        if(optionalProduct.isEmpty()) {
            throw new RuntimeException("Product with id: " + id + " not found");
        }

        return ResponseEntity.ok(ApiResponse.success(200, "Product retrieved successfully", optionalProduct.get()));
    }

    @Operation(summary = "Create product")
    @PostMapping
    ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @Operation(summary = "Update product by id")
    @PutMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete product by id")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(null);
    }
}
