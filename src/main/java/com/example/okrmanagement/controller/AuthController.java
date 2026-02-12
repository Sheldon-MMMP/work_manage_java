package com.example.okrmanagement.controller;

import com.example.okrmanagement.dto.LoginRequest;
import com.example.okrmanagement.dto.RegisterRequest;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.AuthService;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.okrmanagement.common.VerificExceptionHandler;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public SuccessResponse register(@Valid @RequestBody RegisterRequest registerRequest,
            BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            log.info("Registering user: {}", registerRequest.getUsername());
            User user = authService.register(registerRequest);
            log.info("User registered successfully: {}", user.getEmail());

            // 注册成功后自动登录，生成令牌
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(registerRequest.getEmail());
            loginRequest.setPassword(registerRequest.getPassword());
            String token = authService.login(loginRequest);

            java.util.Map<String, String> data = new java.util.HashMap<>();
            data.put("token", token);
            log.info("User automatically logged in after registration: {}", user.getEmail());
            return new SuccessResponse(data);
        } catch (Exception e) {
            log.error("Register failed for email: {}", registerRequest.getEmail());
            throw e;
        }
    }

    @PostMapping("/login")
    public SuccessResponse login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        try {
            // 判断是否有错误
            VerificExceptionHandler.handleVerificationException(bindingResult);
            String token = authService.login(loginRequest);

            log.info("User logged in successfully: {}", loginRequest.getEmail());
            java.util.Map<String, String> data = new java.util.HashMap<>();
            data.put("token", token);
            return new SuccessResponse(data);
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail());
            throw e;
        }
    }
}