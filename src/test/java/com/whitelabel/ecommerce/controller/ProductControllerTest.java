package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ProductResponse;
import com.whitelabel.ecommerce.repository.UserRepository;
import com.whitelabel.ecommerce.service.JwtService;
import com.whitelabel.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "abcd@gmail.com", roles = {"USER"})
    void getProductById_ShouldReturn200AndProductData() throws Exception {
        // Arrange
        Long productId = 1L;
        ProductResponse mockResponse = new ProductResponse(
                productId,
                "Phone",
                "A good phone",
                999,
                null,
                null,
                null,
                1L,
                "Electronics"
                );

        when(productService.getProductById(productId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.title").value("Phone"))
                .andExpect(jsonPath("$.data.description").value("A good phone"))
                .andExpect(jsonPath("$.data.price").value(999))
                .andExpect(jsonPath("$.data.category_id").value(1L))
                .andExpect(jsonPath("$.data.category_name").value("Electronics"));
    }
}
