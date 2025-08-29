package com.example.song_service.mapper;

import com.example.song_service.dto.SongDto;
import com.example.song_service.entities.Song;

public class SongMapper {
    public static Song toEntity(SongDto dto) {
        return Song.builder()
                .id(dto.getId())
                .name(dto.getName())
                .artist(dto.getArtist())
                .album(dto.getAlbum())
                .duration(dto.getDuration())
                .year(dto.getYear())
                .build();
    }

    public static SongDto toDto(Song entity) {
        return SongDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .artist(entity.getArtist())
                .album(entity.getAlbum())
                .duration(entity.getDuration())
                .year(entity.getYear())
                .build();
    }
}
