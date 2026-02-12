package com.example.okrmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.common.TypeValidator;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.TimeRecord;
import com.example.okrmanagement.service.TimeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SuccessResponse recordTime(@PathVariable String taskId, @RequestBody TimeRecord timeRecord) {
        TypeValidator.validatePathParam("taskId", taskId, Long.class);
        Long parsedTaskId = Long.parseLong(taskId);
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Recording time for task {} by user {}", parsedTaskId, userId);
        try {
            TimeRecord newTimeRecord = timeRecordService.recordTime(parsedTaskId, timeRecord);
            log.info("Time record created successfully: {}", newTimeRecord.getId());
            return new SuccessResponse(newTimeRecord);
        } catch (Exception e) {
            log.error("Record time failed for task {} by user {}", parsedTaskId, userId, e);
            throw e;
        }
    }

    @GetMapping("/recent")
    public SuccessResponse getRecentTimeRecords() {
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Getting recent time records for user: {}", userId);
        try {
            List<TimeRecord> timeRecords = timeRecordService.getRecentTimeRecords();
            log.info("Got {} recent time records for user: {}", timeRecords.size(), userId);
            return new SuccessResponse(timeRecords);
        } catch (Exception e) {
            log.error("Get recent time records failed for user: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("/today")
    public SuccessResponse getTodayTimeRecords() {
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Getting today's time records for user: {}", userId);
        try {
            List<TimeRecord> timeRecords = timeRecordService.getTodayTimeRecords();
            log.info("Got {} time records for today for user: {}", timeRecords.size(), userId);
            return new SuccessResponse(timeRecords);
        } catch (Exception e) {
            log.error("Get today's time records failed for user: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("/today/summary")
    public SuccessResponse getTodaySummary() {
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Getting today's summary for user: {}", userId);
        try {
            Map<String, Object> summary = timeRecordService.getTodaySummary();
            log.info("Got today's summary for user: {}", userId);
            return new SuccessResponse(summary);
        } catch (Exception e) {
            log.error("Get today's summary failed for user: {}", userId, e);
            throw e;
        }
    }
}
