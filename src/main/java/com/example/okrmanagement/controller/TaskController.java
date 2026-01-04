package com.example.okrmanagement.controller;

import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/key-results")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/{krId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long krId, @RequestBody Task task, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Task newTask = taskService.createTask(krId, task, user);
        return ResponseEntity.ok(newTask);
    }

    @GetMapping("/{krId}/tasks")
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long krId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Task> tasks = taskService.getTasks(krId, user);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Task updatedTask = taskService.updateTask(id, task, user);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }
}