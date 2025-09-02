package com.example.resource_service.exception_handler;

import com.example.resource_service.exceptions.BadRequestException;
import com.example.resource_service.exceptions.InvalidMp3Exception;
import com.example.resource_service.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.builder()
                        .errorMessage(ex.getMessage())
                        .errorCode("404")
                        .build()
                );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Validation error: " + ex.getMessage())
                        .errorCode("400")
                        .details(Map.of("request", ex.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var invalidValue = ex.getValue().toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Validation error: Invalid ID format")
                        .errorCode("400")
                        .details(Map.of("id", "ID must be a positive integer", "providedId", invalidValue))
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        var details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
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

        ApiError.ApiErrorBuilder builder = ApiError.builder()
                .errorCode(code);

        if (status.is4xxClientError()) {
            builder.errorMessage("Validation error");
        } else {
            builder.errorMessage("Downstream service error");
        }

        return ResponseEntity.status(status).body(builder.build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Validation error")
                        .errorCode("400")
                        .details(Map.of("request", "Malformed JSON or incorrect data type"))
                        .build()
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        var details = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Validation error")
                        .errorCode("400")
                        .details(details)
                        .build()
                );
    }

    @ExceptionHandler(InvalidMp3Exception.class)
    public ResponseEntity<ApiError> handleInvalidMp3(InvalidMp3Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Invalid MP3 file")
                        .errorCode("400")
                        .details(Map.of("request", ex.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .errorMessage("Unsupported media type")
                        .errorCode("400")
                        .details(Map.of("request", "Unsupported content type"))
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.builder()
                        .errorMessage("Internal server error")
                        .errorCode("500")
                        .build()
                );
    }
}
