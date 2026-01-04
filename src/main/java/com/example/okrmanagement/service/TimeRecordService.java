package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.TimeRecord;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.TaskRepository;
import com.example.okrmanagement.repository.TimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimeRecordService {
    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Autowired
    private TaskRepository taskRepository;

    public TimeRecord recordTime(Long taskId, TimeRecord timeRecord, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getKeyResult().getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to record time for this task");
        }

        timeRecord.setTask(task);
        timeRecord.setUser(user);
        return timeRecordRepository.save(timeRecord);
    }

    public List<TimeRecord> getRecentTimeRecords(User user) {
        return timeRecordRepository.findByUserIdOrderByStartTimeDesc(user.getId());
    }

    public List<TimeRecord> getTodayTimeRecords(User user) {
        return timeRecordRepository.findTodayRecordsByUserId(user.getId());
    }

    public Map<String, Object> getTodaySummary(User user) {
        List<TimeRecord> todayRecords = timeRecordRepository.findTodayRecordsByUserId(user.getId());

        // 计算总时长
        Duration totalDuration = todayRecords.stream()
                .map(record -> Duration.between(record.getStartTime(), record.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);

        // 按任务分组计算时长
        Map<String, Duration> taskDurations = todayRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getTask().getTitle(),
                        Collectors.mapping(record -> Duration.between(record.getStartTime(), record.getEndTime()),
                                Collectors.reducing(Duration.ZERO, Duration::plus))
                ));

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
                                        "minutes", entry.getValue().toMinutes() % 60
                                )
                        ))
        );
    }
}