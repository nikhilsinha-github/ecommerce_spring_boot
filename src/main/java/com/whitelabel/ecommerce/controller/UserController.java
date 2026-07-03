package com.whitelabel.ecommerce.controller;

import com.whitelabel.ecommerce.dto.ApiResponse;
import com.whitelabel.ecommerce.dto.UserResponse;
import com.whitelabel.ecommerce.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.whitelabel.ecommerce.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "Users", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                200,
                "All users retrieved successfully",
                userService.getAllUsers(pageNumber, size)
        ));
    }

}
