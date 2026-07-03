package com.whitelabel.ecommerce.dto;

import com.whitelabel.ecommerce.entity.Product;

import java.util.List;

public record ProductResponse(
        Long id,
        String title,
        String description,
        double price,
        List<String> images,
        String size,
        String color,
        Long category_id,
        String category_name) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getImages(),
                product.getSize(),
                product.getColor(),
                product.getCategory() != null? product.getCategory().getId() : 0,
                product.getCategory() != null ? product.getCategory().getTitle() : "Uncategorized"
        );
    }
}
