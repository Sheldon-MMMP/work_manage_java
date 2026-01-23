package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@permissionEvaluator.hasKeyResultPermission(#krId)")
    public Task createTask(Long krId, Task task) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        task.setKrId(krId);
        task.setStatus("todo");
        return taskRepository.save(task);
    }

    @PreAuthorize("@permissionEvaluator.hasKeyResultPermission(#krId)")
    public List<Task> getTasks(Long krId) {
        keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        return taskRepository.findByKrId(krId);
    }



    @PreAuthorize("@permissionEvaluator.hasTaskPermission(#taskId)")
    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());

        return taskRepository.save(task);
    }

    @PreAuthorize("@permissionEvaluator.hasTaskPermission(#taskId)")
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));

        taskRepository.delete(task);
    }
}