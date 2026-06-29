package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.ProductRequest;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.entity.Product;
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

    private final ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Page<Product>> getProductByTitle(int pageNumber, int size, String title) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return productRepository.findByTitle(pageable, title);
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImages(request.images());
        product.setSize(request.size());
        product.setColor(request.color());

        Category category = entityManager.getReference(Category.class, request.category_id());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequest request) {

        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isEmpty()) {
            throw new RuntimeException("No product found with id: " + id);
        }

        Product product = optionalProduct.get();

        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImages(request.images());
        product.setSize(request.size());
        product.setColor(request.color());

        Category category = entityManager.getReference(Category.class, request.category_id());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProduct() {
        productRepository.deleteAll();
    }
}
