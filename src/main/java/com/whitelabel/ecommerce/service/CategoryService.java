package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.CategoryRequest;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> getAllCategories(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return categoryRepository.findAll(pageable);
    }

    public Optional<Page<Category>> getCategoryByTitle(int pageNumber, int size, String title) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return categoryRepository.findByTitle(pageable, title);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setTitle(request.title());
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest request) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        Category category = optionalCategory.get();
        category.setTitle(request.title());
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
