package com.example.resource_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ResourceFileDto {
    @NotBlank
    private String filename;

    @NotBlank
    private String filePath;

    @NotNull
    @Positive
    private Long size;

    @NotBlank
    private String contentType;
}