package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.ProductRequest;
import com.whitelabel.ecommerce.dto.ProductResponse;
import com.whitelabel.ecommerce.entity.Category;
import com.whitelabel.ecommerce.entity.Product;
import com.whitelabel.ecommerce.exception.NotFoundException;
import com.whitelabel.ecommerce.repository.CategoryRepository;
import com.whitelabel.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_WhenProductsExists_ShouldReturnPaginatedProducts() {
        // Arrange
        int pageNumber = 0;
        int size = 10;

        Product p1 = new Product();
        p1.setId(1L);
        p1.setTitle("Laptop");
        p1.setPrice(1200.00);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setTitle("Smartphone");
        p2.setPrice(800.00);

        Page<Product> mockProductPage = new PageImpl<>(List.of(p1, p2), PageRequest.of(pageNumber, size), 2);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(mockProductPage);

        // Act
        Page<ProductResponse> result = productService.getAllProducts(pageNumber, size);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        assertThat(result.getContent().get(0).title()).isEqualTo("Laptop");
        assertThat(result.getContent().get(1).title()).isEqualTo("Smartphone");
        assertThat(result.getTotalElements()).isEqualTo(2);

        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAllProducts_WhenNoProducts_ShouldEmptyPage() {
        // Arrange
        int pageNumber = 0;
        int size = 10;

        // Create an empty page
        Page<Product> emptyPage = new PageImpl<>(List.of(), PageRequest.of(pageNumber, size), 0);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<ProductResponse> result = productService.getAllProducts(pageNumber, size);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findAll(pageableArgumentCaptor.capture());

        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertThat(capturedPage.getPageNumber()).isEqualTo(0);
        assertThat(capturedPage.getPageSize()).isEqualTo(10);
    }

    @Test
    void getProductByTitle_WhenMatchesExist_ShouldReturnPaginatedResponse() {
        // Arrange
        String searchTitle = "Laptop";
        int pageNumber = 0;
        int size = 10;

        Product p1 = new Product();
        p1.setTitle("Gaming Laptop");
        p1.setDescription("A Windows laptop");
        p1.setPrice(49999);

        Product p2 = new Product();
        p2.setTitle("Business Laptop");
        p2.setDescription("A Linux laptop for office work");
        p2.setPrice(40000);

        Page<Product> mockPage = new PageImpl<>(
                List.of(p1, p2),
                PageRequest.of(pageNumber, size),
                2
        );

        when(productRepository.findByTitleContainingIgnoreCase(
                any(Pageable.class),
                eq(searchTitle)
        )).thenReturn(mockPage);

        // Act
        Page<ProductResponse> result = productService.getProductByTitle(pageNumber, size, searchTitle);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        assertThat(result.getContent().get(0).title()).isEqualTo("Gaming Laptop");
        assertThat(result.getContent().get(1).title()).isEqualTo("Business Laptop");
        assertThat(result.getTotalElements()).isEqualTo(2);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(productRepository, times(1)).findByTitleContainingIgnoreCase(
                pageableCaptor.capture(),
                eq(searchTitle)
        );

        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
        assertThat(capturedPageable.getPageSize()).isEqualTo(10);
    }

    @Test
    void getProductByTitle_WhenNoMatchesExist_ShouldReturnEmptyPaginatedResponse() {
        // Arrange
        String searchTitle = "NonExistingProductXYZ";
        int pageNumber = 0;
        int size = 10;

        Page<Product> emptyMockPage = new PageImpl<>(
                List.of(),
                PageRequest.of(pageNumber, size),
                0
        );

        when(productRepository.findByTitleContainingIgnoreCase(
                any(Pageable.class),
                eq(searchTitle)
        )).thenReturn(emptyMockPage);

        // Act
        Page<ProductResponse> result = productService.getProductByTitle(pageNumber, size, searchTitle);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);

        verify(productRepository, times(1))
                .findByTitleContainingIgnoreCase(
                        any(Pageable.class),
                        eq(searchTitle)
                );
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProductResponse() {
        // Arrange
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setTitle("Test laptop");
        mockProduct.setDescription("Windows 11 laptop by Lenovo");
        mockProduct.setPrice(49999);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Act
        ProductResponse result = productService.getProductById(productId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test laptop");
        assertThat(result.description()).isEqualTo("Windows 11 laptop by Lenovo");
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        Long productId = 99L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void createProduct_WithValidRequest_ShouldSaveAndReturnResponse() {
        // Arrange
        ProductRequest request = new ProductRequest(
                "New Watch",
                "Smartwatch",
                250.0,
                List.of("url1"),
                "M",
                "Black",
                1L
        );

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setTitle("Electronics");

        Product savedProduct = new Product();
        savedProduct.setId(100L);
        savedProduct.setTitle(request.title());
        savedProduct.setPrice(request.price());
        savedProduct.setCategory(mockCategory);

        when(categoryRepository.findById(request.category_id())).thenReturn(Optional.of(mockCategory));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductResponse response = productService.createProduct(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.title()).isEqualTo("New Watch");
        assertThat(response.price()).isEqualTo(250.0);
        assertThat(response.category_name()).isEqualTo("Electronics");

        verify(categoryRepository, times(1)).findById(request.category_id());
        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    void createProduct_WhenCategoryNotFound_ShouldThrowNotFoundException() {
        // Arrange
        ProductRequest request = new ProductRequest(
                "New Watch",
                "Smartwatch",
                250.0,
                List.of("url"),
                "M",
                "Black",
                99L
        );

        when(categoryRepository.findById(request.category_id())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.createProduct(request));

        verify(categoryRepository, times(1)).findById(request.category_id());
        verify(productRepository, never()).save(any(Product.class));

    }

    @Test
    void updateProduct_WhenProductAndCategoryExists_ShouldUpdateAndReturnResponse() {
        // Arrange
        Long productId = 100L;
        ProductRequest request = new ProductRequest(
                "Updated Watch",
                "Updated Desc",
                299.0,
                List.of("url2"),
                "L",
                "White",
                1L
        );

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setTitle("Electronics");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setTitle("Old Watch");
        existingProduct.setCategory(mockCategory);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(request.category_id())).thenReturn(Optional.of(mockCategory));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        ProductResponse response = productService.updateProduct(productId, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(existingProduct.getTitle()).isEqualTo("Updated Watch");

        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_WhenCategoryDoesNotExist_ShouldThrowNotFoundException() {
        // 1. Arrange
        Long productId = 100L;
        Long invalidCategoryId = 999L;
        ProductRequest request = new ProductRequest(
                "Updated Watch",
                "Desc",
                299.0,
                List.of("url"),
                "M",
                "Black",
                invalidCategoryId
        );

        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());

        // 2. Act & Assert
        assertThrows(NotFoundException.class, () -> productService.updateProduct(productId, request));

        verify(categoryRepository, times(1)).findById(invalidCategoryId);
        verify(productRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        Long invalidProductId = 999L;
        Long validCategoryId = 1L;
        ProductRequest request = new ProductRequest(
                "Updated Watch",
                "Updated Desc",
                299.0,
                List.of("url2"),
                "L",
                "White",
                1L
        );

        Category mockCategory = new Category();
        mockCategory.setId(validCategoryId);
        mockCategory.setTitle("Electronics");

        when(categoryRepository.findById(validCategoryId)).thenReturn(Optional.of(mockCategory));
        when(productRepository.findById(invalidProductId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.updateProduct(invalidProductId, request));

        verify(categoryRepository, times(1)). findById(validCategoryId);
        verify(productRepository, times(1)).findById(invalidProductId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDeleteSuccessfully() {
        // Arrange
        Long productId = 99L;
        Long validCategoryId = 1L;
        Category mockCategory = new Category();
        mockCategory.setId(validCategoryId);
        mockCategory.setTitle("Electronics");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setTitle("Watch");
        existingProduct.setDescription("Smartwatch");
        existingProduct.setPrice(299.0);
        existingProduct.setCategory(mockCategory);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        Long invalidProductId = 999L;

        when(productRepository.findById(invalidProductId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(invalidProductId));

        verify(productRepository, times(1)).findById(invalidProductId);
        verify(productRepository, never()).deleteById(anyLong());
    }

}