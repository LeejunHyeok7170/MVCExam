package com.demo.mvcexam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private String id;
    private String password;
    private String email;
    private String nickname;
    private Instant createdAt = Instant.now();
}