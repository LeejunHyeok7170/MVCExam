package com.demo.mvcexam.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue
    private UUID id;
    private String originalName;
    private String extension;
    private String path;
    private Long size;
    private Instant uploadedAt = Instant.now();

    public Image(UUID id, String originalName,
                 String extension, String path,
                 long size, Instant uploadedAt) {
        this.id = id;
        this.originalName = originalName;
        this.extension = extension;
        this.path = path;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();
}