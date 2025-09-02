package com.example.resource_service.services;

import com.example.resource_service.entities.ResourceFile;
import com.example.resource_service.exceptions.BadRequestException;
import com.example.resource_service.exceptions.InvalidMp3Exception;
import com.example.resource_service.exceptions.NotFoundException;
import com.example.resource_service.repositories.ResourceFileRepository;
import com.example.resource_service.util.Mp3MetadataExtractor;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceFileService {
    private final ResourceFileRepository repository;
    private final SongServiceClient songClient;

    public Long upload(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new InvalidMp3Exception("MP3 file is empty or missing");
        }

        Metadata md;
        try {
            md = Mp3MetadataExtractor.extract(bytes);
        } catch (IOException | TikaException | SAXException e) {
            throw new InvalidMp3Exception("Invalid MP3 file");
        }

        var saved = repository.save(
                ResourceFile.builder()
                        .data(bytes)
                        .build()
        );

        var songRequest = Mp3MetadataExtractor.toSongRequest(saved.getId(), md);
        songClient.createSongMetadata(songRequest);

        return saved.getId();
    }

    public byte[] getBytes(long id) {
        requirePositiveId(id);
        var entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resource with ID=" + id + " not found"));

        return entity.getData();
    }

    public List<Long> deleteByCsv(String csv) {
        validateCsv(csv);
        var ids = Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::parsePositiveLong)
                .toList();

        var deleted = new ArrayList<Long>();
        ids.stream()
                .filter(repository::existsById)
                .forEach(id -> {
                    repository.deleteById(id);
                    deleted.add(id);
                });

        if (!ids.isEmpty()) {
            songClient.deleteByCsv(
                    ids.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
            );
        }

        return deleted;
    }

    private void validateCsv(String csv) {
        if (csv == null || csv.isBlank()) {
            throw new BadRequestException("Query parameter 'id' (CSV) is required");
        }
        if (csv.length() >= 200) {
            throw new BadRequestException("CSV length must be less than 200 characters (provided: " + csv.length() + " characters)");
        }
        if (!csv.matches("^[0-9,]+$")) {
            throw new BadRequestException("CSV must contain only digits and commas");
        }
    }

    private long parsePositiveLong(String value) {
        try {
            var id = Long.parseLong(value);
            requirePositiveId(id);
            return id;
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID must be a positive integer, provided Id=" + value);
        }
    }

    private void requirePositiveId(long id) {
        if (id <= 0) throw new BadRequestException("ID must be a positive integer, provided Id=" + id);
    }
}
