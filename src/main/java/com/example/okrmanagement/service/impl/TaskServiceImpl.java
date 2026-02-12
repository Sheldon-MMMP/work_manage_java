package com.example.okrmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.mapper.TaskMapper;
import com.example.okrmanagement.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Override
    public Task createTask(Task task) {
        if (task.getUserId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        if (task.getTaskListId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        if (task.getParentId() != null) {
            int level = getTaskLevel(task.getParentId());
            if (level >= 2) {
                throw new BusinessException(ErrorCode.TASK_LEVEL_EXCEEDED);
            }
        }
        if (task.getStatus() == null) {
            task.setStatus("todo");
        }
        save(task);
        log.info("Task created successfully: {}", task.getId());
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (task.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        Task existingTask = getTaskById(task.getId());

        // 更新父任务（含层级校验和循环依赖校验）
        if (task.getParentId() != null && !task.getParentId().equals(existingTask.getParentId())) {
            int level = getTaskLevel(task.getParentId());
            if (level >= 2) {
                throw new BusinessException(ErrorCode.TASK_LEVEL_EXCEEDED);
            }
            if (isAncestor(task.getId(), task.getParentId())) {
                throw new BusinessException(ErrorCode.TASK_CIRCULAR_DEPENDENCY);
            }
            existingTask.setParentId(task.getParentId());
        }

        // 更新基本字段
        if (task.getTitle() != null) {
            existingTask.setTitle(task.getTitle());
        }
        if (task.getDescription() != null) {
            existingTask.setDescription(task.getDescription());
        }
        if (task.getStartTime() != null) {
            existingTask.setStartTime(task.getStartTime());
        }
        if (task.getEndTime() != null) {
            existingTask.setEndTime(task.getEndTime());
        }
        if (task.getTaskListId() != null) {
            existingTask.setTaskListId(task.getTaskListId());
        }

        // 更新状态与完成标记（保持联动同步）
        if (task.getStatus() != null) {
            existingTask.setStatus(task.getStatus());
            if ("done".equals(task.getStatus())) {
                existingTask.setCompleted(true);
            } else if ("todo".equals(task.getStatus()) || "in_progress".equals(task.getStatus())) {
                existingTask.setCompleted(false);
            }
        } else {
            existingTask.setCompleted(task.isCompleted());
            if (task.isCompleted()) {
                existingTask.setStatus("done");
            } else if ("done".equals(existingTask.getStatus())) {
                existingTask.setStatus("todo");
            }
        }

        updateById(existingTask);
        log.info("Task updated successfully: {}", existingTask.getId());
        return existingTask;
    }

    @Override
    public void deleteTask(Long taskId) {
        if (taskId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        getTaskById(taskId);
        List<Task> subTasks = lambdaQuery().eq(Task::getParentId, taskId).list();
        if (!subTasks.isEmpty()) {
            throw new BusinessException(ErrorCode.TASK_HAS_CHILDREN);
        }
        removeById(taskId);
        log.info("Task deleted successfully: {}", taskId);
    }

    @Override
    public Task getTaskById(Long taskId) {
        Task task = getById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        List<Task> subTasks = lambdaQuery().eq(Task::getParentId, taskId).list();
        task.setSubTasks(subTasks);
        return task;
    }

    @Override
    public List<Task> getTasksByTaskListId(Long taskListId) {
        if (taskListId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        List<Task> tasks = lambdaQuery().eq(Task::getTaskListId, taskListId).list();
        for (Task task : tasks) {
            List<Task> subTasks = lambdaQuery().eq(Task::getParentId, task.getId()).list();
            task.setSubTasks(subTasks);
            for (Task subTask : subTasks) {
                List<Task> subSubTasks = lambdaQuery().eq(Task::getParentId, subTask.getId()).list();
                subTask.setSubTasks(subSubTasks);
            }
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByParentId(Long parentId) {
        if (parentId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return lambdaQuery().eq(Task::getParentId, parentId).list();
    }

    private int getTaskLevel(Long taskId) {
        Task task = getById(taskId);
        if (task == null) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }
        if (task.getParentId() == null) {
            return 0;
        }
        return 1 + getTaskLevel(task.getParentId());
    }

    private boolean isAncestor(Long targetTaskId, Long sourceTaskId) {
        Task task = getById(sourceTaskId);
        if (task == null || task.getParentId() == null) {
            return false;
        }
        if (task.getParentId().equals(targetTaskId)) {
            return true;
        }
        return isAncestor(targetTaskId, task.getParentId());
    }
}
