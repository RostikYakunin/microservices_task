package com.example.resource_service.repositories;

import com.example.resource_service.entities.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceFileRepository extends JpaRepository<ResourceFile, Long> {
}
