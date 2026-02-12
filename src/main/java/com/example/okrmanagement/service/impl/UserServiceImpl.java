package com.example.okrmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.mapper.UserMapper;
import com.example.okrmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        User existingUser = getUserById(user.getId());
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(encryptPassword(user.getPassword()));
        }
        updateById(existingUser);
        log.info("User updated successfully: {}", existingUser.getId());
        return existingUser;
    }

    private String encryptPassword(String password) {
        if (password == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = getBaseMapper().findByEmail(email);
        return user.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User updateUserAvatar(Long userId, String avatarUrl) {
        User user = getUserById(userId);
        user.setAvatar(avatarUrl);
        updateById(user);
        log.info("User avatar updated successfully: {}", user.getId());
        return user;
    }
}
