package com.example.resource_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongMetadataRequest {
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}

