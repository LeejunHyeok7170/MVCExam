package com.demo.mvcexam.service;

import com.demo.mvcexam.JWT.JwtUtil;
import com.demo.mvcexam.dto.LoginRequest;
import com.demo.mvcexam.dto.UserRegisterRequest;
import com.demo.mvcexam.entity.User;
import com.demo.mvcexam.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.web.OffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OffsetScrollPositionHandlerMethodArgumentResolver offsetResolver;

    public UserService(UserRepository userRepository, OffsetScrollPositionHandlerMethodArgumentResolver offsetResolver) {
        this.userRepository = userRepository;
        this.offsetResolver = offsetResolver;
    }

    public void register(UserRegisterRequest request) {
        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User user = new User();
        user.setId(request.getId());
        user.setPassword(hashedPassword);
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setCreatedAt(Instant.now());

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 반환
        return new JwtUtil().generateToken(user.getId());
    }

    public boolean existsById(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    // 기타 메서드...
}
