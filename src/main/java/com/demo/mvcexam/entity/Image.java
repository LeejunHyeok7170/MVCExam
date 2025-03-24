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

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();
}