package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.ProductRequest;
import com.whitelabel.ecommerce.dto.ProductResponse;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.entity.Product;
import com.whitelabel.ecommerce.exception.NotFoundException;
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
        Optional<Page<Product>> optionalProducts = productRepository.findByTitleContainingIgnoreCase(pageable, title);

        if(optionalProducts.isEmpty()) {
            throw new NotFoundException("No product found with title: " + title);
        }

        Page<Product> products = optionalProducts.get();
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setImages(request.images());
        product.setSize(request.size());
        product.setColor(request.color());

        Category category = entityManager.getReference(Category.class, request.category_id());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return ProductResponse.fromEntity(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {

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

        Category category = entityManager.getReference(Category.class, request.category_id());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return ProductResponse.fromEntity(updatedProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProduct() {
        productRepository.deleteAll();
    }
}
