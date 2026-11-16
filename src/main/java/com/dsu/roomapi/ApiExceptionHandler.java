package com.dsu.roomapi;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.ErrorResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> invalidFields(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ApiError.FieldMessage> fields = e.getBindingResult()
                .getFieldErrors().stream()
                .map(f -> new ApiError.FieldMessage(f.getField(), f.getDefaultMessage()))
                .toList();
        return respond(HttpStatus.BAD_REQUEST,
                "request has invalid fields", request, fields);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(
            NotFoundException e, HttpServletRequest request) {
        return respond(HttpStatus.NOT_FOUND, e.getMessage(), request, List.of());
    }


    private ResponseEntity<ApiError> respond(HttpStatus status, String message,
                                             HttpServletRequest request, List<ApiError.FieldMessage> fields) {
        return ResponseEntity.status(status)
                .body(ApiError.of(status, message, request.getRequestURI(), fields));
    }

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> other(Exception e, HttpServletRequest request) {
        if (e instanceof ErrorResponse spring) {
            HttpStatus status = HttpStatus.valueOf(spring.getStatusCode().value());
            return respond(status, spring.getBody().getDetail(), request, List.of());
        }

        log.error("Unexpected error on {} {}", request.getMethod(), request.getRequestURI(), e);
        return respond(HttpStatus.INTERNAL_SERVER_ERROR,
                "unexpected server error", request, List.of());
    }
}