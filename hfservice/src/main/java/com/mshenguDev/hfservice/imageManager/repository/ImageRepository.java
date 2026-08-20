package com.mshenguDev.hfservice.imageManager.repository;

import com.mshenguDev.hfservice.imageManager.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findImageByName(String fileName);
}
