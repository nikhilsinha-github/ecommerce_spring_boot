package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ApiResponse;
import com.whitelabel.ecommerce.dto.AuthResponse;
import com.whitelabel.ecommerce.dto.LoginRequest;
import com.whitelabel.ecommerce.dto.RegisterRequest;
import com.whitelabel.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs for authentication")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "User registered successfully",
                        authService.register(request)
                )
        );
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse authResponse = new AuthResponse(authService.login(request));
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Logged in successfully",
                        authResponse
                )
        );
    }
}