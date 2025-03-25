package com.demo.mvcexam.service;

import com.demo.mvcexam.JWT.JwtUtil;
import com.demo.mvcexam.dto.LoginRequest;
import com.demo.mvcexam.dto.UserRegisterRequest;
import com.demo.mvcexam.entity.User;
import com.demo.mvcexam.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.web.OffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

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
        validateUserInput(request.getId(), request.getPassword(),
                request.getEmail(), request.getNickname());

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
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())
        &&user == null && !user.getPassword().
                        equals(encryptPassword(request.getPassword()))) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 반환
        return new JwtUtil().generateToken(user.getId());
    }

    public boolean existsById(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    private void validateUserInput(String userId, String password, String email, String nickname) {
        if (userId.length() < 6 || userId.length() > 30) {
            throw new IllegalArgumentException("ID는 6~30자여야 합니다.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 12~50자이며, 영문/숫자/특수문자(!@#$%^&*)를 각각 2개 이상 포함해야 합니다.");
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email) || email.length() > 100) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않거나 100자를 초과했습니다.");
        }
        if (nickname.length() < 3 || nickname.length() > 50) {
            throw new IllegalArgumentException("닉네임은 3~50자여야 합니다.");
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 12 || password.length() > 50) return false;
        return Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{12,50}$", password);
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비밀번호 암호화 오류", e);
        }
    }
}
