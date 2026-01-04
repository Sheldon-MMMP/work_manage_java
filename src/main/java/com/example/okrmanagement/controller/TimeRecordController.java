package com.example.okrmanagement.controller;

import com.example.okrmanagement.entity.TimeRecord;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.TimeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/time-records")
public class TimeRecordController {
    @Autowired
    private TimeRecordService timeRecordService;

    @PostMapping("/record/{taskId}")
    public ResponseEntity<TimeRecord> recordTime(@PathVariable Long taskId, @RequestBody TimeRecord timeRecord, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        TimeRecord newTimeRecord = timeRecordService.recordTime(taskId, timeRecord, user);
        return ResponseEntity.ok(newTimeRecord);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TimeRecord>> getRecentTimeRecords(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<TimeRecord> timeRecords = timeRecordService.getRecentTimeRecords(user);
        return ResponseEntity.ok(timeRecords);
    }

    @GetMapping("/today")
    public ResponseEntity<List<TimeRecord>> getTodayTimeRecords(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<TimeRecord> timeRecords = timeRecordService.getTodayTimeRecords(user);
        return ResponseEntity.ok(timeRecords);
    }

    @GetMapping("/today/summary")
    public ResponseEntity<Map<String, Object>> getTodaySummary(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Map<String, Object> summary = timeRecordService.getTodaySummary(user);
        return ResponseEntity.ok(summary);
    }
}