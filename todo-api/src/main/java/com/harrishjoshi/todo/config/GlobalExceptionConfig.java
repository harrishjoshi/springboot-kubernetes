package com.harrishjoshi.todo.config;

import com.harrishjoshi.todo.exception.TodoAlreadyExists;
import com.harrishjoshi.todo.exception.TodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(TodoNotFoundException.class)
    ProblemDetail handleTodoNotFound(TodoNotFoundException ex) {
        var problemDetails = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        problemDetails.setTitle("Todo Not Found");
        return problemDetails;
    }

    @ExceptionHandler(TodoAlreadyExists.class)
    ProblemDetail handleTodoAlreadyExists(TodoAlreadyExists ex) {
        var problemDetails = ProblemDetail
                .forStatusAndDetail(HttpStatus.CONFLICT, ex.getLocalizedMessage());
        problemDetails.setTitle("Todo Already Exists");

        return problemDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        var problemDetails = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed. Please check the request.");
        problemDetails.setTitle("Bad Request");
        var errors = ex.getBindingResult().getAllErrors().stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : fieldError.getField() + " is required"));

        problemDetails.setProperty("errors", errors);

        return problemDetails;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleException() {
        var problemDetails = ProblemDetail
                .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again later.");
        problemDetails.setTitle("Internal Server Error");

        return problemDetails;
    }
}