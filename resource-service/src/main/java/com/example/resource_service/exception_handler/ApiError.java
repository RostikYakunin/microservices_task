package com.example.resource_service.exception_handler;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiError {
    private String errorMessage;
    private String errorCode;
    private Map<String, String> details;
}