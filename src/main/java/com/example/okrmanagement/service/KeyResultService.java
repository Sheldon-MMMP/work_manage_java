package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.repository.KeyResultRepository;
import com.example.okrmanagement.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@permissionEvaluator.hasObjectivePermission(#objectiveId)")
    public KeyResult createKeyResult(Long objectiveId, KeyResult keyResult) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        keyResult.setObjective(objective);
        return keyResultRepository.save(keyResult);
    }

    @PreAuthorize("@permissionEvaluator.hasObjectivePermission(#objectiveId)")
    public List<KeyResult> getKeyResults(Long objectiveId) {
        objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBJECTIVE_NOT_FOUND));

        return keyResultRepository.findByObjectiveId(objectiveId);
    }

    @PreAuthorize("@permissionEvaluator.hasKeyResultPermission(#krId)")
    public KeyResult updateKeyResult(Long krId, KeyResult updatedKeyResult) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        keyResult.setTitle(updatedKeyResult.getTitle());
        keyResult.setDescription(updatedKeyResult.getDescription());
        keyResult.setWeight(updatedKeyResult.getWeight());

        return keyResultRepository.save(keyResult);
    }

    @PreAuthorize("@permissionEvaluator.hasKeyResultPermission(#krId)")
    public void deleteKeyResult(Long krId) {
        KeyResult keyResult = keyResultRepository.findById(krId)
                .orElseThrow(() -> new BusinessException(ErrorCode.KEY_RESULT_NOT_FOUND));

        keyResultRepository.delete(keyResult);
    }
}