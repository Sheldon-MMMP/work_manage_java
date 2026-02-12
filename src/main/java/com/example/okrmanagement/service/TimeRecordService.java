package com.example.okrmanagement.service;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.TimeRecord;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.mapper.TaskMapper;
import com.example.okrmanagement.mapper.TimeRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimeRecordService {
    @Autowired
    private TimeRecordMapper timeRecordMapper;

    @Autowired
    private TaskMapper taskMapper;
    /**
     * 记录时间，需具备该任务的权限（任务属于当前用户）
     */
    public TimeRecord recordTime(Long taskId, TimeRecord timeRecord) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        long userId = StpUtil.getLoginIdAsLong();
        if (!task.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        timeRecord.setTaskId(taskId);
        timeRecord.setUserId(userId);
        timeRecordMapper.insert(timeRecord);
        return timeRecord;
    }

    public List<TimeRecord> getRecentTimeRecords() {
        long userId = StpUtil.getLoginIdAsLong();
        return timeRecordMapper.findByUserIdOrderByStartTimeDesc(userId);
    }

    public List<TimeRecord> getTodayTimeRecords() {
        long userId = StpUtil.getLoginIdAsLong();
        return timeRecordMapper.findTodayRecordsByUserId(userId);
    }

    public Map<String, Object> getTodaySummary() {
        long userId = StpUtil.getLoginIdAsLong();
        List<TimeRecord> todayRecords = timeRecordMapper.findTodayRecordsByUserId(userId);

        if (todayRecords.isEmpty()) {
            return Map.of(
                    "totalHours", 0L,
                    "totalMinutes", 0L,
                    "totalMinutesAll", 0L,
                    "taskDurations", Map.<String, Map<String, Long>>of());
        }

        List<Long> taskIds = todayRecords.stream()
                .map(TimeRecord::getTaskId)
                .distinct()
                .toList();
        List<Task> tasks = taskMapper.selectBatchIds(taskIds);
        Map<Long, String> taskIdToTitle = tasks.stream()
                .collect(Collectors.toMap(Task::getId, Task::getTitle));

        Duration totalDuration = todayRecords.stream()
                .map(record -> Duration.between(record.getStartTime(), record.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);

        Map<String, Duration> taskDurations = todayRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> taskIdToTitle.getOrDefault(record.getTaskId(), "未知"),
                        Collectors.mapping(record -> Duration.between(record.getStartTime(), record.getEndTime()),
                                Collectors.reducing(Duration.ZERO, Duration::plus))));

        long totalMinutes = totalDuration.toMinutes();
        long totalHours = totalMinutes / 60;
        long remainingMinutes = totalMinutes % 60;

        return Map.of(
                "totalHours", totalHours,
                "totalMinutes", remainingMinutes,
                "totalMinutesAll", totalMinutes,
                "taskDurations", taskDurations.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Map.<String, Long>of(
                                        "hours", entry.getValue().toHours(),
                                        "minutes", entry.getValue().toMinutes() % 60))));
    }
}
