package com.demo.mvcexam.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequest {
    private String title;
    private String content;
    private List<String> tags;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<String> getTags() { return tags; }
}