package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.ProductRequest;
import com.whitelabel.ecommerce.dto.ProductResponse;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.entity.Product;
import com.whitelabel.ecommerce.exception.NotFoundException;
import com.whitelabel.ecommerce.repository.CategoryRepository;
import com.whitelabel.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository ,ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Page<ProductResponse> getAllProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return productRepository.findAll(pageable)
                .map(ProductResponse::fromEntity);
    }

    public ProductResponse getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isEmpty()) {
            throw new NotFoundException("No product found with id: " + id);
        }

        Product product = optionalProduct.get();
        return ProductResponse.fromEntity(product);
    }

    public Page<ProductResponse> getProductByTitle(int pageNumber, int size, String title) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.findByTitleContainingIgnoreCase(pageable, title);

        return products.map(ProductResponse::fromEntity);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.category_id())
                .orElseThrow(() -> new NotFoundException("Category with id: " + request.category_id() + " not found"));

        Product product = new Product();
        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImages(request.images());
        product.setSize(request.size());
        product.setColor(request.color());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return ProductResponse.fromEntity(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Category category = categoryRepository.findById(request.category_id())
                .orElseThrow(() -> new NotFoundException("Category with id: " + request.category_id() + " not found"));

        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isEmpty()) {
            throw new NotFoundException("No product found with id: " + id);
        }

        Product product = optionalProduct.get();

        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImages(request.images());
        product.setSize(request.size());
        product.setColor(request.color());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return ProductResponse.fromEntity(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No product found with id: " + id));

        productRepository.deleteById(id);
    }

    public void deleteAllProduct() {
        productRepository.deleteAll();
    }
}
