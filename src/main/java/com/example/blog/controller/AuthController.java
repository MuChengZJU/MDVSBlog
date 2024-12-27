package com.example.blog.controller;

import com.example.blog.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        // 从请求体中获取用户名和密码
        String username = credentials.get("username");
        String password = credentials.get("password");

        // 校验参数是否为空
        if (username == null || password == null) {
            throw new RuntimeException("Username or password is missing");
        }

        // 调用服务层方法进行登录
        String token = authService.login(username, password);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
