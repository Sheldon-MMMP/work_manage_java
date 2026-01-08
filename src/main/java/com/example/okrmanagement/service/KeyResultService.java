package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

@Service
public class KeyResultService {
    @Autowired
    private KeyResultRepository keyResultRepository;

    @Autowired
    private ObjectiveRepository objectiveRepository;

    public KeyResult createKeyResult(Long objectiveId, KeyResult keyResult, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !objective.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        keyResult.setObjective(objective);
        return keyResultRepository.save(keyResult);
    }

    public List<KeyResult> getKeyResults(Long objectiveId, User user) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !objective.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        return keyResultRepository.findByObjectiveId(objectiveId);
    }

    public KeyResult updateKeyResult(Long krId, KeyResult updatedKeyResult, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        keyResult.setTitle(updatedKeyResult.getTitle());
        keyResult.setDescription(updatedKeyResult.getDescription());
        keyResult.setWeight(updatedKeyResult.getWeight());

        return keyResultRepository.save(keyResult);
    }

    public void deleteKeyResult(Long krId, User user) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !keyResult.getObjective().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        keyResultRepository.delete(keyResult);
    }
}