package com.demo.mvcexam.service;

import com.demo.mvcexam.entity.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
public class ImageService {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB
    private static final String UPLOAD_DIR = "uploads/";

    private final Map<UUID, Image> imageStore = new HashMap<>();

    public UUID uploadImage(MultipartFile file) throws IOException {
        validateImage(file);

        String originalName = Objects.requireNonNull(file.getOriginalFilename());
        String extension = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
        UUID imageId = UUID.randomUUID();
        String savedPath = UPLOAD_DIR + imageId + "." + extension;

        File savedFile = new File(savedPath);
        file.transferTo(savedFile);

        Image image = new Image(imageId, originalName, extension, savedPath, file.getSize(), Instant.now());
        imageStore.put(imageId, image);

        return imageId;
    }

    public Image getImage(UUID imageId) {
        return imageStore.get(imageId);
    }

    private void validateImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.length() > 36) {
            throw new IllegalArgumentException("파일명이 너무 깁니다 (최대 32자 + 확장자)");
        }
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 1MB 이하여야 합니다.");
        }
    }
}
