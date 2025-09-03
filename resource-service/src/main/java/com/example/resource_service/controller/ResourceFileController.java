package com.example.resource_service.controller;

import com.example.resource_service.dtos.DeletedResourcesResponse;
import com.example.resource_service.dtos.UploadResponse;
import com.example.resource_service.services.ResourceFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceFileController {
    private final ResourceFileService service;

    @PostMapping
    public ResponseEntity<UploadResponse> upload(
            @RequestBody byte[] body,
            @RequestHeader(value = "Content-Type", required = false) String contentType
    ) {
        var id = service.upload(body, contentType);
        return ResponseEntity.ok(new UploadResponse(id));
    }

    @GetMapping(value = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> get(@PathVariable long id) {
        var data = service.getBytes(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .contentLength(data.length)
                .body(data);
    }

    @DeleteMapping
    public ResponseEntity<DeletedResourcesResponse> delete(@RequestParam String id) {
        var deletedIds = service.deleteByCsv(id);
        return ResponseEntity.ok(new DeletedResourcesResponse(deletedIds));
    }
}
