package com.example.okrmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.okrmanagement.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
