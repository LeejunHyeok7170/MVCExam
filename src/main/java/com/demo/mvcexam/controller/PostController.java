package com.demo.mvcexam.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequest request, HttpServletRequest httpRequest) {
        // 인터셉터에서 설정한 사용자 ID 가져오기
        String userId = (String) httpRequest.getAttribute("userId");

        // 서비스 호출하여 게시물 생성
        String postId = postService.createPost(request, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("postId", postId);

        return ResponseEntity.ok(response);
    }

    // 기타 엔드포인트...
}
