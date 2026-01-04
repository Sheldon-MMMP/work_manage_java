package com.example.okrmanagement.controller;

import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ObjectiveController {
    @Autowired
    private ObjectiveService objectiveService;

    @PostMapping("/objectives")
    public ResponseEntity<Objective> createObjective(@RequestBody Objective objective, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Objective newObjective = objectiveService.createObjective(objective, user);
        return ResponseEntity.ok(newObjective);
    }

    @GetMapping("/objectives/active")
    public ResponseEntity<List<Objective>> getActiveObjectives(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Objective> objectives = objectiveService.getActiveObjectives(user);
        return ResponseEntity.ok(objectives);
    }

    @GetMapping("/objectives")
    public ResponseEntity<List<Objective>> getAllObjectives(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Objective> objectives = objectiveService.getAllObjectives(user);
        return ResponseEntity.ok(objectives);
    }

    @PutMapping("/objectives/{id}")
    public ResponseEntity<Objective> updateObjective(@PathVariable Long id, @RequestBody Objective objective, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Objective updatedObjective = objectiveService.updateObjective(id, objective, user);
        return ResponseEntity.ok(updatedObjective);
    }

    @DeleteMapping("/objectives/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        objectiveService.deleteObjective(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/objectives/{id}/archive")
    public ResponseEntity<Objective> archiveObjective(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Objective archivedObjective = objectiveService.archiveObjective(id, user);
        return ResponseEntity.ok(archivedObjective);
    }
}