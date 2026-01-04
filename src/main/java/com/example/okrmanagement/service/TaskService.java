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
                .orElseThrow(() -> new RuntimeException("Key Result not found"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to add task to this KR");
        }

        task.setKeyResult(keyResult);
        task.setStatus("todo");
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Long krId, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("Key Result not found"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to view tasks for this KR");
        }

        return taskRepository.findByKeyResultId(krId);
    }

    public Task updateTask(Long taskId, Task updatedTask, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getKeyResult().getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this task");
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
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getKeyResult().getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this task");
        }

        taskRepository.delete(task);
    }
}