package com.example.resource_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeletedResourcesResponse {
    private List<Long> ids;
}
