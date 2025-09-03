package com.example.song_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongDto {

    @NotNull(message = "Song id is required")
    private Long id;

    @NotBlank(message = "Song name is required")
    private String name;

    @NotBlank(message = "Artist is required")
    private String artist;

    @NotBlank(message = "Album is required")
    private String album;

    @Pattern(
            regexp = "^[0-5]\\d:[0-5]\\d$",
            message = "Duration must be in mm:ss format with leading zeros"
    )
    private String duration;

    @Pattern(
            regexp = "^(19\\d{2}|20\\d{2})$",
            message = "Year must be between 1900 and 2099"
    )
    private String year;
}
