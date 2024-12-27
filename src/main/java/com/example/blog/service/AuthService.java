package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import java.util.Date;
import java.util.Optional;


import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 使用符合 HS512 要求的密钥
    private final Key jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 天有效期
                    .signWith(jwtSecret) // 使用安全密钥签名
                    .compact();
        }
        throw new RuntimeException("Invalid username or password");
    }
}
