package com.example.okrmanagement.controller;

import com.example.okrmanagement.common.TypeValidator;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.TimeRecord;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.TimeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/time-records")
public class TimeRecordController {
    @Autowired
    private TimeRecordService timeRecordService;

    @PostMapping("/record/{taskId}")
    public SuccessResponse recordTime(@PathVariable String taskId, @RequestBody TimeRecord timeRecord, Authentication authentication) {
        TypeValidator.validatePathParam("taskId", taskId, Long.class);
        Long parsedTaskId = Long.parseLong(taskId);
        User user = (User) authentication.getPrincipal();
        log.info("Recording time for task {} by user {}", parsedTaskId, user.getUsername());
        try {
            TimeRecord newTimeRecord = timeRecordService.recordTime(parsedTaskId, timeRecord, user);
            log.info("Time record created successfully: {}", newTimeRecord.getId());
            return new SuccessResponse(newTimeRecord);
        } catch (Exception e) {
            log.error("Record time failed for task {} by user {}", parsedTaskId, user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/recent")
    public SuccessResponse getRecentTimeRecords(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting recent time records for user: {}", user.getUsername());
        try {
            List<TimeRecord> timeRecords = timeRecordService.getRecentTimeRecords(user);
            log.info("Got {} recent time records for user: {}", timeRecords.size(), user.getUsername());
            return new SuccessResponse(timeRecords);
        } catch (Exception e) {
            log.error("Get recent time records failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/today")
    public SuccessResponse getTodayTimeRecords(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting today's time records for user: {}", user.getUsername());
        try {
            List<TimeRecord> timeRecords = timeRecordService.getTodayTimeRecords(user);
            log.info("Got {} time records for today for user: {}", timeRecords.size(), user.getUsername());
            return new SuccessResponse(timeRecords);
        } catch (Exception e) {
            log.error("Get today's time records failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/today/summary")
    public SuccessResponse getTodaySummary(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting today's summary for user: {}", user.getUsername());
        try {
            Map<String, Object> summary = timeRecordService.getTodaySummary(user);
            log.info("Got today's summary for user: {}", user.getUsername());
            return new SuccessResponse(summary);
        } catch (Exception e) {
            log.error("Get today's summary failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }
}