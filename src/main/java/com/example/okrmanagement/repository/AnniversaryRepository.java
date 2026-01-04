package com.example.okrmanagement.repository;

import com.example.okrmanagement.entity.Anniversary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnniversaryRepository extends JpaRepository<Anniversary, Long> {
    List<Anniversary> findByUserId(Long userId);
}