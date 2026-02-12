package com.example.okrmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.TaskList;
import com.example.okrmanagement.service.TaskListService;
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
@RequestMapping("/api/v1/task-lists")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    /**
     * 创建清单
     */
    @PostMapping
    public SuccessResponse createTaskList(@Valid @RequestBody TaskList taskList, BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Long userId = StpUtil.getLoginIdAsLong();
            taskList.setUserId(userId);
            log.info("Creating task list for user: {}", userId);

            TaskList createdTaskList = taskListService.createTaskList(taskList);
            log.info("Task list created successfully: {}", createdTaskList.getId());

            return new SuccessResponse(createdTaskList);
        } catch (Exception e) {
            log.error("Failed to create task list: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 更新清单
     */
    @PutMapping("/{taskListId}")
    public SuccessResponse updateTaskList(@Valid @RequestBody TaskList taskList, @PathVariable Long taskListId, BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Long userId = StpUtil.getLoginIdAsLong();
            taskList.setId(taskListId);
            taskList.setUserId(userId);
            log.info("Updating task list: {}", taskListId);

            TaskList updatedTaskList = taskListService.updateTaskList(taskList);
            log.info("Task list updated successfully: {}", updatedTaskList.getId());

            return new SuccessResponse(updatedTaskList);
        } catch (Exception e) {
            log.error("Failed to update task list: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 删除清单
     */
    @DeleteMapping("/{taskListId}")
    public SuccessResponse deleteTaskList(@PathVariable Long taskListId) {
        try {
            log.info("Deleting task list: {}", taskListId);
            taskListService.deleteTaskList(taskListId);
            log.info("Task list deleted successfully: {}", taskListId);

            return new SuccessResponse(null);
        } catch (Exception e) {
            log.error("Failed to delete task list: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取清单详情
     */
    @GetMapping("/{taskListId}")
    public SuccessResponse getTaskList(@PathVariable Long taskListId) {
        try {
            log.info("Getting task list: {}", taskListId);
            TaskList taskList = taskListService.getTaskListById(taskListId);
            log.info("Task list retrieved successfully: {}", taskList.getId());

            return new SuccessResponse(taskList);
        } catch (Exception e) {
            log.error("Failed to get task list: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户的所有清单
     */
    @GetMapping
    public SuccessResponse getTaskLists() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            log.info("Getting task lists for user: {}", userId);
            List<TaskList> taskLists = taskListService.getTaskListsByUserId(userId);
            log.info("Retrieved {} task lists for user: {}", taskLists.size(), userId);

            return new SuccessResponse(taskLists);
        } catch (Exception e) {
            log.error("Failed to get task lists: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取指定文件夹下的清单
     */
    @GetMapping("/folder/{folderId}")
    public SuccessResponse getTaskListsByFolder(@PathVariable Long folderId) {
        try {
            log.info("Getting task lists by folder: {}", folderId);
            List<TaskList> taskLists = taskListService.getTaskListsByFolderId(folderId);
            log.info("Retrieved {} task lists for folder: {}", taskLists.size(), folderId);

            return new SuccessResponse(taskLists);
        } catch (Exception e) {
            log.error("Failed to get task lists by folder: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 移动清单到新的文件夹
     */
    @PutMapping("/{taskListId}/move")
    public SuccessResponse moveTaskList(@PathVariable Long taskListId, @RequestParam(required = false) Long newFolderId) {
        try {
            log.info("Moving task list: {} to folder: {}", taskListId, newFolderId);
            taskListService.moveTaskList(taskListId, newFolderId);
            log.info("Task list moved successfully: {}", taskListId);

            return new SuccessResponse(null);
        } catch (Exception e) {
            log.error("Failed to move task list: {}", e.getMessage());
            throw e;
        }
    }
}
