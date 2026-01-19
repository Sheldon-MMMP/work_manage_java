package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.User;

public interface UserService {
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户实体
     */
    User getUserById(Long id);

    /**
     * 更新用户信息
     * @param user 用户实体
     * @return 更新后的用户实体
     */
    User updateUser(User user);



    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户实体
     */
    User getUserByEmail(String email);

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 更新后的用户实体
     */
    User updateUserAvatar(Long userId, String avatarUrl);

    /**
     * 根据uuId获取用户信息
     * @param uuId 用户uuId
     * @return 用户实体
     */
    User getUserByUuId(String uuId);
}