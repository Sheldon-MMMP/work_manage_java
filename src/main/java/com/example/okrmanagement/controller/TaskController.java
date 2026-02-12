package com.example.okrmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.service.TaskService;
import com.example.okrmanagement.common.VerificExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 创建任务
     */
    @PostMapping
    public SuccessResponse createTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Long userId = StpUtil.getLoginIdAsLong();
            task.setUserId(userId);
            log.info("Creating task for user: {}", userId);

            Task createdTask = taskService.createTask(task);
            log.info("Task created successfully: {}", createdTask.getId());

            return new SuccessResponse(createdTask);
        } catch (Exception e) {
            log.error("Failed to create task: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 更新任务
     */
    @PutMapping("/{taskId}")
    public SuccessResponse updateTask(@Valid @RequestBody Task task, @PathVariable Long taskId, BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Long userId = StpUtil.getLoginIdAsLong();
            task.setId(taskId);
            task.setUserId(userId);
            log.info("Updating task: {}", taskId);

            Task updatedTask = taskService.updateTask(task);
            log.info("Task updated successfully: {}", updatedTask.getId());

            return new SuccessResponse(updatedTask);
        } catch (Exception e) {
            log.error("Failed to update task: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{taskId}")
    public SuccessResponse deleteTask(@PathVariable Long taskId) {
        try {
            log.info("Deleting task: {}", taskId);
            taskService.deleteTask(taskId);
            log.info("Task deleted successfully: {}", taskId);

            return new SuccessResponse(null);
        } catch (Exception e) {
            log.error("Failed to delete task: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{taskId}")
    public SuccessResponse getTask(@PathVariable Long taskId) {
        try {
            log.info("Getting task: {}", taskId);
            Task task = taskService.getTaskById(taskId);
            log.info("Task retrieved successfully: {}", task.getId());

            return new SuccessResponse(task);
        } catch (Exception e) {
            log.error("Failed to get task: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取指定清单下的任务
     */
    @GetMapping("/task-list/{taskListId}")
    public SuccessResponse getTasksByTaskList(@PathVariable Long taskListId) {
        try {
            log.info("Getting tasks by task list: {}", taskListId);
            List<Task> tasks = taskService.getTasksByTaskListId(taskListId);
            log.info("Retrieved {} tasks for task list: {}", tasks.size(), taskListId);

            return new SuccessResponse(tasks);
        } catch (Exception e) {
            log.error("Failed to get tasks by task list: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取指定父任务下的子任务
     */
    @GetMapping("/parent/{parentId}")
    public SuccessResponse getTasksByParent(@PathVariable Long parentId) {
        try {
            log.info("Getting tasks by parent: {}", parentId);
            List<Task> tasks = taskService.getTasksByParentId(parentId);
            log.info("Retrieved {} tasks for parent: {}", tasks.size(), parentId);

            return new SuccessResponse(tasks);
        } catch (Exception e) {
            log.error("Failed to get tasks by parent: {}", e.getMessage());
            throw e;
        }
    }

}
