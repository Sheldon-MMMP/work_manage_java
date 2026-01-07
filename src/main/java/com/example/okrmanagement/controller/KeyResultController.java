package com.example.okrmanagement.controller;

import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.KeyResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class KeyResultController {
    @Autowired
    private KeyResultService keyResultService;

    @PostMapping("/objectives/{objectiveId}/key-results")
    public SuccessResponse createKeyResult(@PathVariable Long objectiveId, @RequestBody KeyResult keyResult, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Creating key result for objective {} by user {}", objectiveId, user.getUsername());
        try {
            KeyResult newKeyResult = keyResultService.createKeyResult(objectiveId, keyResult, user);
            log.info("Key result created successfully: {}", newKeyResult.getId());
            return new SuccessResponse(newKeyResult);
        } catch (Exception e) {
            log.error("Create key result failed for objective {} by user {}", objectiveId, user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/objectives/{objectiveId}/key-results")
    public SuccessResponse getKeyResults(@PathVariable Long objectiveId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting key results for objective {} by user {}", objectiveId, user.getUsername());
        try {
            List<KeyResult> keyResults = keyResultService.getKeyResults(objectiveId, user);
            log.info("Got {} key results for objective {}", keyResults.size(), objectiveId);
            return new SuccessResponse(keyResults);
        } catch (Exception e) {
            log.error("Get key results failed for objective {} by user {}", objectiveId, user.getUsername(), e);
            throw e;
        }
    }

    @PutMapping("/key-results/{id}")
    public SuccessResponse updateKeyResult(@PathVariable Long id, @RequestBody KeyResult keyResult, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Updating key result {} by user {}", id, user.getUsername());
        try {
            KeyResult updatedKeyResult = keyResultService.updateKeyResult(id, keyResult, user);
            log.info("Key result updated successfully: {}", updatedKeyResult.getId());
            return new SuccessResponse(updatedKeyResult);
        } catch (Exception e) {
            log.error("Update key result failed for id {} by user {}", id, user.getUsername(), e);
            throw e;
        }
    }

    @DeleteMapping("/key-results/{id}")
    public SuccessResponse deleteKeyResult(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Deleting key result {} by user {}", id, user.getUsername());
        try {
            keyResultService.deleteKeyResult(id, user);
            log.info("Key result deleted successfully: {}", id);
            return new SuccessResponse();
        } catch (Exception e) {
            log.error("Delete key result failed for id {} by user {}", id, user.getUsername(), e);
            throw e;
        }
    }
}