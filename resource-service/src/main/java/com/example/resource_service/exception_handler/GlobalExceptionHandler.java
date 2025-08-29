package com.example.resource_service.exception_handler;

import com.example.resource_service.exceptions.BadRequestException;
import com.example.resource_service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ApiError.builder()
                                .errorMessage(ex.getMessage())
                                .errorCode("400")
                                .build()
                );
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiError.builder()
                                .errorMessage("Validation error")
                                .errorCode("400")
                                .details(Map.of("request", ex.getMessage()))
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiError.builder()
                                .errorMessage("Validation error")
                                .errorCode("400")
                                .details(Map.of("id", "ID must be a positive integer"))
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        var details = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> details.put(fe.getField(), fe.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiError.builder()
                                .errorMessage("Validation error")
                                .errorCode("400")
                                .details(details)
                                .build()
                );
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiError> handleDownstream(RestClientResponseException ex) {
        var status = HttpStatus.resolve(ex.getStatusCode().value());
        var code = String.valueOf(status.value());
        var message = status.is4xxClientError()
                ? "Validation error"
                : "Downstream service error";

        return ResponseEntity.status(status)
                .body(
                        ApiError.builder()
                                .errorMessage(message)
                                .errorCode(code)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiError.builder()
                                .errorMessage("Internal server error")
                                .errorCode("500")
                                .build()
                );
    }
}
