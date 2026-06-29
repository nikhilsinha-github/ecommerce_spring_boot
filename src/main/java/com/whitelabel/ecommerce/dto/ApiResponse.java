package com.whitelabel.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public record ApiResponse<T>(int status, String message, T data, Object error) {

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(200, message, data, null);
    }

    public static <T> ApiResponse<T> error(int status, String message, Object errorDetails) {
        return new ApiResponse<>(status, message, null, errorDetails);
    }
}
