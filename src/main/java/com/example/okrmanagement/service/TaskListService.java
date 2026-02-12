package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.TaskList;
import java.util.List;

public interface TaskListService {
    /**
     * 创建清单
     */
    TaskList createTaskList(TaskList taskList);

    /**
     * 更新清单
     */
    TaskList updateTaskList(TaskList taskList);

    /**
     * 删除清单
     */
    void deleteTaskList(Long taskListId);

    /**
     * 获取清单详情
     */
    TaskList getTaskListById(Long taskListId);

    /**
     * 获取用户的所有清单
     */
    List<TaskList> getTaskListsByUserId(Long userId);

    /**
     * 获取指定文件夹下的清单
     */
    List<TaskList> getTaskListsByFolderId(Long folderId);

    /**
     * 移动清单到新的文件夹
     */
    void moveTaskList(Long taskListId, Long newFolderId);
}
