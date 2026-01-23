package com.example.okrmanagement.security;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.Task;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.ObjectiveRepository;
import com.example.okrmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.okrmanagement.entity.User;

@Component
public class PermissionEvaluator {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private KeyResultRepository keyResultRepository;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 检查当前用户是否有权限访问指定目标
     * @param objectiveId 目标ID
     * @return 是否有权限
     */
    public boolean hasObjectivePermission(Long objectiveId) {
        if (objectiveId == null) {
            return false;
        }
        
        Objective objective = objectiveRepository.findById(objectiveId).orElse(null);
        if (objective == null) {
            return false;
        }
        
        return hasUserPermission(objective.getUserId());
    }

    /**
     * 检查当前用户是否有权限访问指定关键结果
     * @param krId 关键结果ID
     * @return 是否有权限
     */
    public boolean hasKeyResultPermission(Long krId) {
        if (krId == null) {
            return false;
        }
        
        KeyResult keyResult = keyResultRepository.findById(krId).orElse(null);
        if (keyResult == null) {
            return false;
        }
        
        // 先获取关键结果所属的目标，再检查目标所属的用户
        Objective objective = keyResult.getObjective();
        if (objective == null) {
            return false;
        }
        
        return hasUserPermission(objective.getUserId());
    }

    /**
     * 检查当前用户是否有权限访问指定任务
     * @param taskId 任务ID
     * @return 是否有权限
     */
    public boolean hasTaskPermission(Long taskId) {
        if (taskId == null) {
            return false;
        }
        
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return false;
        }
        
        // 获取任务所属的关键结果，再获取关键结果所属的目标，最后检查目标所属的用户
        KeyResult keyResult = keyResultRepository.findById(task.getKrId()).orElse(null);
        if (keyResult == null) {
            return false;
        }
        
        Objective objective = keyResult.getObjective();
        if (objective == null) {
            return false;
        }
        
        return hasUserPermission(objective.getUserId());
    }

    /**
     * 检查当前用户是否与指定用户ID匹配
     * @param userId 用户ID
     * @return 是否匹配
     */
    private boolean hasUserPermission(Long userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            return user.getId().equals(userId);
        }
        return false;
    }
}