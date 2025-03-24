package com.demo.mvcexam.service;

import com.demo.mvcexam.dto.PostCreateRequest;
import com.demo.mvcexam.entity.Post;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PostService {
    private final Map<UUID, Post> posts = new HashMap<>();

    public String createPost(PostCreateRequest request, String userId) {
        Post post = new Post(UUID.randomUUID(), request.getTitle(),
                request.getContent(),
                UUID.fromString(userId), request.getTags(),
                Instant.now(), Instant.now());
        posts.put(post.getId(), post);
        return post.getId().toString();
    }
}
