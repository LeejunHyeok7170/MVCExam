package com.demo.mvcexam.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter

public class Post {
    private UUID id;
    private String title;
    private String content;
    private UUID authorId;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;


    public Post(UUID id, String title, String content, UUID authorId, List<String> tags, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}