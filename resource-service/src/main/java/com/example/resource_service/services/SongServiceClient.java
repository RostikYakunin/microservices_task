package com.example.resource_service.services;

import com.example.resource_service.dtos.SongMetadataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SongServiceClient {
    private final RestClient restClient;
    private final DiscoveryClient discoveryClient;

    @Value("${song-service.base-url}")
    private String baseUrl;

    public void createSongMetadata(SongMetadataRequest body) {
        restClient.post()
                .uri(getBaseUrl() + "/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteByCsv(String csv) {
        restClient.delete()
                .uri(getBaseUrl() + "/songs?id={csv}", csv)
                .retrieve()
                .toBodilessEntity();
    }

    private String getBaseUrl() {
        var instances = discoveryClient.getInstances("song-service");
        if (instances.isEmpty()) {
            throw new IllegalStateException("Song service is not registered in Eureka");
        }

        if (!baseUrl.contains("localhost")) return baseUrl;

        var instance = instances.get(0);
        var host = instance.getHost();
        var port = instance.getPort();

        return "http://" + host + ":" + port;
    }
}
