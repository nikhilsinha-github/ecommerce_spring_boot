package com.whitelabel.ecommerce.dto;

import com.whitelabel.ecommerce.entity.User;

public record UserResponse(
        String email,
        String firstname,
        String lastname,
        int age,
        String role
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                user.getAge(),
                user.getRole()
        );
    }
}
