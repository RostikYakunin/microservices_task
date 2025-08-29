package com.example.resource_service.controller;

import com.example.resource_service.services.ResourceFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceFileController {
    private final ResourceFileService service;

    @PostMapping(consumes = "audio/mpeg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> upload(@RequestBody byte[] mp3) {
        var id = service.upload(mp3);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping(value = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> get(@PathVariable long id) {
        var data = service.getBytes(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(data);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Long>>> delete(@RequestParam("id") String csv) {
        var deleted = service.deleteByCsv(csv);
        return ResponseEntity.ok(Map.of("ids", deleted));
    }
}
