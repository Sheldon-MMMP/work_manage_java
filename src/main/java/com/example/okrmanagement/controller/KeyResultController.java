package com.example.okrmanagement.controller;

import com.example.okrmanagement.entity.KeyResult;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.KeyResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class KeyResultController {
    @Autowired
    private KeyResultService keyResultService;

    @PostMapping("/objectives/{objectiveId}/key-results")
    public ResponseEntity<KeyResult> createKeyResult(@PathVariable Long objectiveId, @RequestBody KeyResult keyResult, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        KeyResult newKeyResult = keyResultService.createKeyResult(objectiveId, keyResult, user);
        return ResponseEntity.ok(newKeyResult);
    }

    @GetMapping("/objectives/{objectiveId}/key-results")
    public ResponseEntity<List<KeyResult>> getKeyResults(@PathVariable Long objectiveId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<KeyResult> keyResults = keyResultService.getKeyResults(objectiveId, user);
        return ResponseEntity.ok(keyResults);
    }

    @PutMapping("/key-results/{id}")
    public ResponseEntity<KeyResult> updateKeyResult(@PathVariable Long id, @RequestBody KeyResult keyResult, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        KeyResult updatedKeyResult = keyResultService.updateKeyResult(id, keyResult, user);
        return ResponseEntity.ok(updatedKeyResult);
    }

    @DeleteMapping("/key-results/{id}")
    public ResponseEntity<Void> deleteKeyResult(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        keyResultService.deleteKeyResult(id, user);
        return ResponseEntity.noContent().build();
    }
}