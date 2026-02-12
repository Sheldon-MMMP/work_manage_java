package com.example.okrmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.okrmanagement.entity.TaskList;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.mapper.TaskListMapper;
import com.example.okrmanagement.service.TaskListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskListServiceImpl extends ServiceImpl<TaskListMapper, TaskList> implements TaskListService {

    @Override
    public TaskList createTaskList(TaskList taskList) {
        if (taskList.getUserId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        if (taskList.getName() == null || taskList.getName().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        List<TaskList> existingTaskLists = lambdaQuery()
                .eq(TaskList::getUserId, taskList.getUserId())
                .eq(TaskList::getFolderId, taskList.getFolderId())
                .eq(TaskList::getName, taskList.getName())
                .list();
        if (!existingTaskLists.isEmpty()) {
            throw new BusinessException(ErrorCode.TASK_LIST_NAME_EXISTS);
        }
        save(taskList);
        log.info("TaskList created successfully: {}", taskList.getId());
        return taskList;
    }

    @Override
    public TaskList updateTaskList(TaskList taskList) {
        if (taskList.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        TaskList existingTaskList = getTaskListById(taskList.getId());
        if (taskList.getName() != null) {
            existingTaskList.setName(taskList.getName());
        }
        if (taskList.getDescription() != null) {
            existingTaskList.setDescription(taskList.getDescription());
        }
        if (taskList.getFolderId() != null) {
            existingTaskList.setFolderId(taskList.getFolderId());
        }
        updateById(existingTaskList);
        log.info("TaskList updated successfully: {}", existingTaskList.getId());
        return existingTaskList;
    }

    @Override
    public void deleteTaskList(Long taskListId) {
        if (taskListId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        getTaskListById(taskListId);
        removeById(taskListId);
        log.info("TaskList deleted successfully: {}", taskListId);
    }

    @Override
    public TaskList getTaskListById(Long taskListId) {
        TaskList taskList = getById(taskListId);
        if (taskList == null) {
            throw new BusinessException(ErrorCode.TASK_LIST_NOT_FOUND);
        }
        return taskList;
    }

    @Override
    public List<TaskList> getTaskListsByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return lambdaQuery().eq(TaskList::getUserId, userId).list();
    }

    @Override
    public List<TaskList> getTaskListsByFolderId(Long folderId) {
        return lambdaQuery().eq(TaskList::getFolderId, folderId).list();
    }

    @Override
    public void moveTaskList(Long taskListId, Long newFolderId) {
        if (taskListId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        TaskList taskList = getTaskListById(taskListId);
        taskList.setFolderId(newFolderId);
        updateById(taskList);
        log.info("TaskList moved successfully: {} to folder {}", taskListId, newFolderId);
    }
}
