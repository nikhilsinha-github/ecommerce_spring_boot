package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ApiResponse;
import com.whitelabel.ecommerce.dto.ProductRequest;
import com.whitelabel.ecommerce.dto.ProductResponse;
import com.whitelabel.ecommerce.entity.Product;
import com.whitelabel.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
    ) {
            Page<ProductResponse> products = (title != null && !title.isEmpty())
                    ? productService.getProductByTitle(pageNumber, size, title)
                    : productService.getAllProducts(pageNumber, size);
            return ResponseEntity.ok(
                    ApiResponse.success(
                            200,
                            "Products retrieved successfully",
                            products
                    )
            );
    }

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Product retrieved successfully",
                        product
                )
        );
    }

    @Operation(summary = "Create product")
    @PostMapping
    ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        201,
                        "Product created successfully",
                        productService.createProduct(request)
                ));
    }

    @Operation(summary = "Update product by id")
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Product updated successfully",
                        updatedProduct
                )
        );
    }

    @Operation(summary = "Delete product by id")
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Product deleted successfully",
                        null
                )
        );
    }
}
