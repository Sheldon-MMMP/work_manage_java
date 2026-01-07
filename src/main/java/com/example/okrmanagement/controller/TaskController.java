package com.example.okrmanagement.controller;

import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/key-results")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/{krId}/tasks")
    public SuccessResponse createTask(@PathVariable Long krId, @RequestBody Task task, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Creating task for KR {} by user {}", krId, user.getUsername());
        try {
            Task newTask = taskService.createTask(krId, task, user);
            log.info("Task created successfully: {}", newTask.getId());
            return new SuccessResponse(newTask);
        } catch (Exception e) {
            log.error("Create task failed for KR {} by user {}", krId, user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/{krId}/tasks")
    public SuccessResponse getTasks(@PathVariable Long krId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting tasks for KR {} by user {}", krId, user.getUsername());
        try {
            List<Task> tasks = taskService.getTasks(krId, user);
            log.info("Got {} tasks for KR {}", tasks.size(), krId);
            return new SuccessResponse(tasks);
        } catch (Exception e) {
            log.error("Get tasks failed for KR {} by user {}", krId, user.getUsername(), e);
            throw e;
        }
    }

    @PutMapping("/tasks/{id}")
    public SuccessResponse updateTask(@PathVariable Long id, @RequestBody Task task, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Updating task {} by user {}", id, user.getUsername());
        try {
            Task updatedTask = taskService.updateTask(id, task, user);
            log.info("Task updated successfully: {}", updatedTask.getId());
            return new SuccessResponse(updatedTask);
        } catch (Exception e) {
            log.error("Update task failed for id {} by user {}", id, user.getUsername(), e);
            throw e;
        }
    }

    @DeleteMapping("/tasks/{id}")
    public SuccessResponse deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Deleting task {} by user {}", id, user.getUsername());
        try {
            taskService.deleteTask(id, user);
            log.info("Task deleted successfully: {}", id);
            return new SuccessResponse();
        } catch (Exception e) {
            log.error("Delete task failed for id {} by user {}", id, user.getUsername(), e);
            throw e;
        }
    }
}