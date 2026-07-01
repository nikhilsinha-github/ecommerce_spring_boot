package com.whitelabel.ecommerce.exception;

import com.whitelabel.ecommerce.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(UserAlreadyPresentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handleUserAlreadyPresent(UserAlreadyPresentException ex) {
        return ApiResponse.error(409, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFoundException(NotFoundException ex) {
        return ApiResponse.error(404, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> handleAuthenticationFailureException(AuthenticationFailedException ex) {
        return ApiResponse.error(401, ex.getMessage());
    }
}
