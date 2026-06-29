package com.whitelabel.ecommerce.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(UserAlreadyPresentException.class)
    public RuntimeException handleUserAlreadyPresent(UserAlreadyPresentException ex) {
        throw new RuntimeException(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public RuntimeException handleNotFoundException(NotFoundException ex) {
        throw new RuntimeException(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public RuntimeException handleAuthenticationFailureException(AuthenticationFailedException ex) {
        throw new RuntimeException(ex.getMessage());
    }
}
