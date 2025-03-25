package com.demo.mvcexam.controller;

import com.demo.mvcexam.dto.PostCreateRequest;
import com.demo.mvcexam.entity.Post;
import com.demo.mvcexam.service.ImageService;
import com.demo.mvcexam.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequest request, @RequestHeader("Authorization") String token) {
        String userId = validateToken(token);
        String postId = postService.createPost(request, userId);
        return ResponseEntity.ok(Map.of("postId", postId));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getPosts(page, size));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable UUID postId, @RequestHeader("Authorization") String token) {
        String userId = validateToken(token);
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("게시물이 삭제되었습니다.");
    }

    private String validateToken(String token) {
        return token.replace("Bearer ", "");
    }
}
