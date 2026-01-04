package com.example.okrmanagement.repository;

import com.example.okrmanagement.entity.TimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, Long> {
    List<TimeRecord> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<TimeRecord> findByUserIdOrderByStartTimeDesc(Long userId);
    
    @Query("SELECT tr FROM TimeRecord tr WHERE tr.user.id = ?1 AND FUNCTION('DATE', tr.startTime) = CURRENT_DATE")
    List<TimeRecord> findTodayRecordsByUserId(Long userId);
}