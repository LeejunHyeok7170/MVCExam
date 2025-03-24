package com.demo.mvcexam.dto;

import lombok.Getter;

@Getter

public class UserRegisterRequest
{
    String id;
    String password;
    String email;
    String nickname;
}
