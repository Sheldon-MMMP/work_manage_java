package com.example.okrmanagement.service;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.dto.LoginRequest;
import com.example.okrmanagement.dto.RegisterRequest;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.okrmanagement.common.ErrorCode;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    public User register(RegisterRequest registerRequest) {
        if (Boolean.TRUE.equals(userMapper.existsByEmail(registerRequest.getEmail()))) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS);
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encryptPassword(registerRequest.getPassword()));
        userMapper.insert(user);
        return user;
    }

    public String login(LoginRequest loginRequest) {
        User user = userMapper.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!checkPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INCORRECT_PASSWORD);
        }
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    private String encryptPassword(String password) {
        if (password == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

    private boolean checkPassword(String rawPassword, String encryptedPassword) {
        if (rawPassword == null || encryptedPassword == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return encryptedPassword.equals(DigestUtils.md5DigestAsHex(rawPassword.getBytes(StandardCharsets.UTF_8)));
    }
}
