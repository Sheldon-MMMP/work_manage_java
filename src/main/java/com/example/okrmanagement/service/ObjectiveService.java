package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return objectiveRepository.findByUserIdAndStatus(user.getId(), "active");
    }

    public List<Objective> getAllObjectives(User user) {
        return objectiveRepository.findByUserId(user.getId());
    }

    public Objective updateObjective(Long objectiveId, Objective updatedObjective, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("这个目标不存在"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限更改这个目标");
        }

        objective.setTitle(updatedObjective.getTitle());
        objective.setDescription(updatedObjective.getDescription());
        objective.setAward(updatedObjective.getAward());

        return objectiveRepository.save(objective);
    }

    public void deleteObjective(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("这个目标不存在"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限删除这个目标");
        }

        objectiveRepository.delete(objective);
    }

    public Objective archiveObjective(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("这个目标不存在"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限归档这个目标");
        }

        objective.setStatus("archived");
        return objectiveRepository.save(objective);
    }
}