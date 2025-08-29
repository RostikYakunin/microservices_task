package com.example.resource_service.services;

import com.example.resource_service.dtos.SongMetadataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SongServiceClient {
    private final RestClient restClient;

    @Value("${song-service.base-url:http://localhost:8082}")
    private String baseUrl;

    public void createSongMetadata(SongMetadataRequest body) {
        restClient.post()
                .uri(baseUrl + "/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteByCsv(String csv) {
        restClient.delete()
                .uri(baseUrl + "/songs?id={csv}", csv)
                .retrieve()
                .toBodilessEntity();
    }
}
