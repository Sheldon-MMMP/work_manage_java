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
                .orElseThrow(() -> new RuntimeException("Objective not found"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to add KR to this objective");
        }

        keyResult.setObjective(objective);
        return keyResultRepository.save(keyResult);
    }

    public List<KeyResult> getKeyResults(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new RuntimeException("Objective not found"));

        if (!objective.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to view KRs for this objective");
        }

        return keyResultRepository.findByObjectiveId(objectiveId);
    }

    public KeyResult updateKeyResult(Long krId, KeyResult updatedKeyResult, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("Key Result not found"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this KR");
        }

        keyResult.setTitle(updatedKeyResult.getTitle());
        keyResult.setDescription(updatedKeyResult.getDescription());
        keyResult.setWeight(updatedKeyResult.getWeight());

        return keyResultRepository.save(keyResult);
    }

    public void deleteKeyResult(Long krId, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new RuntimeException("Key Result not found"));

        if (!keyResult.getObjective().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this KR");
        }

        keyResultRepository.delete(keyResult);
    }
}