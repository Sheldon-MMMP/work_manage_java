package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private KeyResultRepository keyResultRepository;

    public Task createTask(Long krId, Task task, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("这个KR不存在"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限添加任务到这个KR");
        }

        task.setKeyResult(keyResult);
        task.setStatus("todo");
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Long krId, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("这个KR不存在"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限查看这个KR的任务");
        }

        return taskRepository.findByKeyResultId(krId);
    }

    public Task updateTask(Long taskId, Task updatedTask, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("这个任务不存在"));

        if (!task.getKeyResult().getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限更新这个任务");
        }

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("这个任务不存在"));

        if (!task.getKeyResult().getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限删除这个任务");
        }

        taskRepository.delete(task);
    }
}