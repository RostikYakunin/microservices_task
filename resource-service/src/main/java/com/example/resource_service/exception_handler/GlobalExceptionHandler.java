package com.example.resource_service.exception_handler;

import com.example.resource_service.dtos.ApiError;
import com.example.resource_service.exceptions.BadRequestException;
import com.example.resource_service.exceptions.InvalidFileFormatException;
import com.example.resource_service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.builder()
                        .errorMessage(ex.getMessage())
                        .errorCode("404")
                        .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage(ex.getMessage())
                        .errorCode("400")
                        .build());
    }

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ApiError> handleInvalidFileFormat(InvalidFileFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage(ex.getMessage())
                        .errorCode("400")
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var value = ex.getValue().toString();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Invalid value '" + value + "' for ID. Must be a positive integer")
                        .errorCode("400")
                        .build());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiError> handleDownstream(RestClientResponseException ex) {
        var status = HttpStatus.resolve(ex.getStatusCode().value());
        var msg = status.is4xxClientError() ? "Validation error" : "Downstream service error";

        return ResponseEntity.status(status)
                .body(ApiError.builder()
                        .errorMessage(msg)
                        .errorCode(String.valueOf(status.value()))
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Validation error")
                        .errorCode("400")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.builder()
                        .errorMessage("Internal server error")
                        .errorCode("500")
                        .build());
    }
}
