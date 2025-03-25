package com.demo.mvcexam.service;

import com.demo.mvcexam.dto.PostCreateRequest;
import com.demo.mvcexam.entity.Post;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service

public class PostService {
    private final Map<UUID, Post> posts = new HashMap<>();
    private final Map<UUID, UUID> postImages = new HashMap<>();

    public String createPost(PostCreateRequest request, String userId) {
        validatePostInput(request);

        UUID postId = UUID.randomUUID();
        Post post = new Post(postId, request.getTitle(), request.getContent(), UUID.fromString(userId), request.getTags(), Instant.now(), Instant.now());
        posts.put(postId, post);

        return postId.toString();
    }

    public List<Post> getPosts(int page, int size) {
        return new ArrayList<>(posts.values()).subList(page * size, Math.min((page + 1) * size, posts.size()));
    }

    public Post getPost(UUID postId) {
        return posts.get(postId);
    }

    public void deletePost(UUID postId, String userId) {
        Post post = posts.get(postId);
        if (post == null || !post.getAuthorId().toString().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        posts.remove(postId);
    }

    private void validatePostInput(PostCreateRequest request) {
        if (request.getTitle().length() < 2 || request.getTitle().length() > 100) {
            throw new IllegalArgumentException("제목은 2~100자여야 합니다.");
        }
        if (request.getContent().length() < 2 || request.getContent().length() > 1000) {
            throw new IllegalArgumentException("내용은 2~1000자여야 합니다.");
        }
        for (String tag : request.getTags()) {
            if (!tag.matches("^[a-zA-Z]+$")) {
                throw new IllegalArgumentException("태그는 영문만 가능합니다.");
            }
        }
    }
}
