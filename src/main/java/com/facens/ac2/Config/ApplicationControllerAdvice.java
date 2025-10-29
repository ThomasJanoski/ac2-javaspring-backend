package com.facens.ac2.Config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

import com.facens.ac2.DTOs.ApiErrorDTO;
import com.facens.ac2.DTOs.RegraNegocioException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice

public class ApplicationControllerAdvice {
    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleRegraNegocioException(RegraNegocioException ex) {
        return new ApiErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDTO handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ApiErrorDTO(ex.getMessage());
    }
}