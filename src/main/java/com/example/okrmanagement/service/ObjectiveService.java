package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

@Service
public class ObjectiveService {
    @Autowired
    private ObjectiveRepository objectiveRepository;

    public Objective createObjective(Objective objective) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        objective.setUserId(user.getId());
        objective.setStatus("active");
        return objectiveRepository.save(objective);
    }

    public List<Objective> getActiveObjectives() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return objectiveRepository.findByUserIdAndStatus(userId, "active");
    }

    public List<Objective> getAllObjectives() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return objectiveRepository.findByUserId(userId);
    }

    @PreAuthorize("@permissionEvaluator.hasObjectivePermission(#objectiveId)")
    public Objective updateObjective(Long objectiveId, Objective updatedObjective) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        objective.setTitle(updatedObjective.getTitle());
        objective.setDescription(updatedObjective.getDescription());
        objective.setAward(updatedObjective.getAward());

        return objectiveRepository.save(objective);
    }

    @PreAuthorize("@permissionEvaluator.hasObjectivePermission(#objectiveId)")
    public void deleteObjective(Long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        objectiveRepository.delete(objective);
    }

    @PreAuthorize("@permissionEvaluator.hasObjectivePermission(#objectiveId)")
    public Objective archiveObjective(Long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        objective.setStatus("archived");
        return objectiveRepository.save(objective);
    }
    
    @PreAuthorize("@permissionEvaluator.hasObjectivePermission(#objectiveId)")
    public Objective getObjectiveById(Long objectiveId) {
        return objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));
    }
}