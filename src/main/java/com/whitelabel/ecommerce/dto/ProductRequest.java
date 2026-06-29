package com.whitelabel.ecommerce.dto;

import com.whitelabel.ecommerce.entity.Category;

import java.util.List;

public record ProductRequest(
        String title,
        String description,
        double price,
        List<String> images,
        String size,
        String color,
        Long category_id
) {
}
