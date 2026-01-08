package com.example.okrmanagement.controller;

import com.example.okrmanagement.dto.JwtResponse;
import com.example.okrmanagement.dto.LoginRequest;
import com.example.okrmanagement.dto.RegisterRequest;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.AuthService;

import jakarta.validation.Valid;

import com.example.okrmanagement.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
            BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            log.info("Registering user: {}", registerRequest.getUsername());
            User user = authService.register(registerRequest);
            log.info("User registered successfully: {}", user.getEmail());

            // 注册成功后自动登录，生成JWT令牌
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            JwtResponse jwtResponse = new JwtResponse(jwt, user.getUsername(), user.getEmail());
            log.info("User automatically logged in after registration: {}", user.getEmail());
            return ResponseEntity.ok(new SuccessResponse(jwtResponse));
        } catch (Exception e) {
            log.error("Register failed for email: {}", registerRequest.getEmail());
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        try {
            // 判断是否有错误
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = (User) userDetails;

            JwtResponse jwtResponse = new JwtResponse(jwt, user.getUsername(), user.getEmail());
            log.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(new SuccessResponse(jwtResponse));
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail());
            throw e;
        }
    }
}