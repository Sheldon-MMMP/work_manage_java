package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private KeyResultRepository keyResultRepository;

    public Task createTask(Long krId, Task task, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        task.setKeyResult(keyResult);
        task.setStatus("todo");
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Long krId, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        return taskRepository.findByKeyResultId(krId);
    }

    public Task updateTask(Long taskId, Task updatedTask, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !task.getKeyResult().getObjective().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
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
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !task.getKeyResult().getObjective().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        taskRepository.delete(task);
    }
}