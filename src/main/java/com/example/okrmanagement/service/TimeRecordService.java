package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.TimeRecord;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.TaskRepository;
import com.example.okrmanagement.repository.TimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

@Service
public class TimeRecordService {
    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Autowired
    private TaskRepository taskRepository;

    @PreAuthorize("@permissionEvaluator.hasTaskPermission(#taskId)")
    public TimeRecord recordTime(Long taskId, TimeRecord timeRecord) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        timeRecord.setTask(task);
        timeRecord.setUser(user);
        return timeRecordRepository.save(timeRecord);
    }

    public List<TimeRecord> getRecentTimeRecords() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return timeRecordRepository.findByUserIdOrderByStartTimeDesc(userId);
    }

    public List<TimeRecord> getTodayTimeRecords() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return timeRecordRepository.findTodayRecordsByUserId(userId);
    }

    public Map<String, Object> getTodaySummary() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        List<TimeRecord> todayRecords = timeRecordRepository.findTodayRecordsByUserId(userId);

        // 计算总时长
        Duration totalDuration = todayRecords.stream()
                .map(record -> Duration.between(record.getStartTime(), record.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);

        // 按任务分组计算时长
        Map<String, Duration> taskDurations = todayRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getTask().getTitle(),
                        Collectors.mapping(record -> Duration.between(record.getStartTime(), record.getEndTime()),
                                Collectors.reducing(Duration.ZERO, Duration::plus))));

        // 转换为小时和分钟
        long totalMinutes = totalDuration.toMinutes();
        long totalHours = totalMinutes / 60;
        long remainingMinutes = totalMinutes % 60;

        // 构建结果
        return Map.of(
                "totalHours", totalHours,
                "totalMinutes", remainingMinutes,
                "totalMinutesAll", totalMinutes,
                "taskDurations", taskDurations.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Map.of(
                                        "hours", entry.getValue().toHours(),
                                        "minutes", entry.getValue().toMinutes() % 60))));
    }
}