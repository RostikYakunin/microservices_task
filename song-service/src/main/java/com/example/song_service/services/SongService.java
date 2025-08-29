package com.example.song_service.services;

import com.example.song_service.dto.SongDto;
import com.example.song_service.exception.AlreadyExistsException;
import com.example.song_service.exception.NotFoundException;
import com.example.song_service.mapper.SongMapper;
import com.example.song_service.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository repository;

    public Long createSong(SongDto dto) {
        if (repository.existsById(dto.getId())) {
            throw new AlreadyExistsException("Metadata for resource ID=" + dto.getId() + " already exists");
        }

        var song = SongMapper.toEntity(dto);
        repository.save(song);

        return song.getId();
    }

    public SongDto getSong(Long id) {
        return repository.findById(id)
                .map(SongMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Song metadata with ID=" + id + " not found"));
    }

    public List<Long> deleteSongs(List<Long> ids) {
        var deleted = new ArrayList<Long>();
        ids.stream()
                .filter(repository::existsById)
                .forEach(id -> {
                    repository.deleteById(id);
                    deleted.add(id);
                });

        return deleted;
    }
}
