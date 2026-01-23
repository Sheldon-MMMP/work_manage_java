package com.example.okrmanagement.service.impl;

import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.repository.UserRepository;
import com.example.okrmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }

        // 验证用户是否存在
        User existingUser = getUserById(user.getId());

        // 更新用户信息（只更新非空字段）
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        if(user.getPassword()!=null){
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully: {}", updatedUser.getId());
        
        return updatedUser;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User updateUserAvatar(Long userId, String avatarUrl) {
        User user = getUserById(userId);
        user.setAvatar(avatarUrl);
        User updatedUser = userRepository.save(user);
        log.info("User avatar updated successfully: {}", updatedUser.getId());
        return updatedUser;
    }


}