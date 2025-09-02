package com.example.song_service.controllers;

import com.example.song_service.dto.SongDto;
import com.example.song_service.services.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {
    private final SongService service;

    @PostMapping
    public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody SongDto dto) {
        var id = service.createSong(dto);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSong(id));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> delete(@RequestParam("id") String ids) {
        var deleted = service.deleteSongs(ids);
        return ResponseEntity.ok(Map.of("ids", deleted));
    }
}
