package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

@Service
public class ObjectiveService {
    @Autowired
    private ObjectiveRepository objectiveRepository;

    public Objective createObjective(Objective objective, User user) {
        objective.setUser(user);
        objective.setStatus("active");
        return objectiveRepository.save(objective);
    }

    public List<Objective> getActiveObjectives(User user) {
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return objectiveRepository.findByUserIdAndStatus(userId, "active");
    }

    public List<Objective> getAllObjectives(User user) {
        Long userId = user.getId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return objectiveRepository.findByUserId(userId);
    }

    public Objective updateObjective(Long objectiveId, Objective updatedObjective, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !objective.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        objective.setTitle(updatedObjective.getTitle());
        objective.setDescription(updatedObjective.getDescription());
        objective.setAward(updatedObjective.getAward());

        return objectiveRepository.save(objective);
    }

    public void deleteObjective(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !objective.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        objectiveRepository.delete(objective);
    }

    public Objective archiveObjective(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !objective.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        objective.setStatus("archived");
        return objectiveRepository.save(objective);
    }
}