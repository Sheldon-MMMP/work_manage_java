package com.example.okrmanagement.repository;

import com.example.okrmanagement.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    List<Objective> findByUserIdAndStatus(Long userId, String status);
    List<Objective> findByUserId(Long userId);
}