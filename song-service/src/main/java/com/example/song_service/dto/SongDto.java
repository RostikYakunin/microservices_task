package com.example.song_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDto {

    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be 1-100 characters")
    private String name;

    @NotBlank(message = "Artist is required")
    @Size(min = 1, max = 100, message = "Artist must be 1-100 characters")
    private String artist;

    @NotBlank(message = "Album is required")
    @Size(min = 1, max = 100, message = "Album must be 1-100 characters")
    private String album;

    @NotBlank(message = "Duration is required")
    @Pattern(regexp = "^[0-5]\\d:[0-5]\\d$",
            message = "Duration must be in mm:ss format with leading zeros")
    private String duration;

    @NotBlank(message = "Year is required")
    @Pattern(regexp = "^(19\\d{2}|20\\d{2})$",
            message = "Year must be between 1900 and 2099")
    private String year;
}
