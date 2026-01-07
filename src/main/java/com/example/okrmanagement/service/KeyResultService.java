package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyResultService {
    @Autowired
    private KeyResultRepository keyResultRepository;

    @Autowired
    private ObjectiveRepository objectiveRepository;

    public KeyResult createKeyResult(Long objectiveId, KeyResult keyResult, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("这个目标不存在"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限添加KR到这个目标");
        }

        keyResult.setObjective(objective);
        return keyResultRepository.save(keyResult);
    }

    public List<KeyResult> getKeyResults(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("这个目标不存在"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限查看这个目标的KR");
        }

        return keyResultRepository.findByObjectiveId(objectiveId);
    }

    public KeyResult updateKeyResult(Long krId, KeyResult updatedKeyResult, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("这个KR不存在"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限更新这个KR");
        }

        keyResult.setTitle(updatedKeyResult.getTitle());
        keyResult.setDescription(updatedKeyResult.getDescription());
        keyResult.setWeight(updatedKeyResult.getWeight());

        return keyResultRepository.save(keyResult);
    }

    public void deleteKeyResult(Long krId, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("这个KR不存在"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("你没有权限删除这个KR");
        }

        keyResultRepository.delete(keyResult);
    }
}