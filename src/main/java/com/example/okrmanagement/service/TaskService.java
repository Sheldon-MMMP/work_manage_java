package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Task;
import java.util.List;

public interface TaskService {
    /**
     * 创建任务
     */
    Task createTask(Task task);

    /**
     * 更新任务
     */
    Task updateTask(Task task);

    /**
     * 删除任务
     */
    void deleteTask(Long taskId);

    /**
     * 获取任务详情
     */
    Task getTaskById(Long taskId);

    /**
     * 获取指定清单下的任务
     */
    List<Task> getTasksByTaskListId(Long taskListId);

    /**
     * 获取指定父任务下的子任务
     */
    List<Task> getTasksByParentId(Long parentId);

}
