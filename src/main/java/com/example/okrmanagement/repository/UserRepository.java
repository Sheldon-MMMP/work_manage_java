package com.example.okrmanagement.repository;

import com.example.okrmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUuId(String uuId);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUuId(String uuId);
}