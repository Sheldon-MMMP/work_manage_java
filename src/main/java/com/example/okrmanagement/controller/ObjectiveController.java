package com.example.okrmanagement.controller;

import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.Objective;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.ObjectiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ObjectiveController {
    @Autowired
    private ObjectiveService objectiveService;

    @PostMapping("/objectives")
    public SuccessResponse createObjective(@RequestBody Objective objective, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Creating objective by user: {}", user.getUsername());
        try {
            Objective newObjective = objectiveService.createObjective(objective, user);
            log.info("Objective created successfully: {}", newObjective.getId());
            return new SuccessResponse(newObjective);
        } catch (Exception e) {
            log.error("Create objective failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/objectives/active")
    public SuccessResponse getActiveObjectives(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting active objectives for user: {}", user.getUsername());
        try {
            List<Objective> objectives = objectiveService.getActiveObjectives(user);
            log.info("Got {} active objectives for user: {}", objectives.size(), user.getUsername());
            return new SuccessResponse(objectives);
        } catch (Exception e) {
            log.error("Get active objectives failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping("/objectives")
    public SuccessResponse getAllObjectives(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting all objectives for user: {}", user.getUsername());
        try {
            List<Objective> objectives = objectiveService.getAllObjectives(user);
            log.info("Got {} objectives for user: {}", objectives.size(), user.getUsername());
            return new SuccessResponse(objectives);
        } catch (Exception e) {
            log.error("Get all objectives failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @PutMapping("/objectives/{id}")
    public SuccessResponse updateObjective(@PathVariable Long id, @RequestBody Objective objective, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Updating objective {} by user: {}", id, user.getUsername());
        try {
            Objective updatedObjective = objectiveService.updateObjective(id, objective, user);
            log.info("Objective updated successfully: {}", updatedObjective.getId());
            return new SuccessResponse(updatedObjective);
        } catch (Exception e) {
            log.error("Update objective failed for id {} by user: {}", id, user.getUsername(), e);
            throw e;
        }
    }

    @DeleteMapping("/objectives/{id}")
    public SuccessResponse deleteObjective(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Deleting objective {} by user: {}", id, user.getUsername());
        try {
            objectiveService.deleteObjective(id, user);
            log.info("Objective deleted successfully: {}", id);
            return new SuccessResponse();
        } catch (Exception e) {
            log.error("Delete objective failed for id {} by user: {}", id, user.getUsername(), e);
            throw e;
        }
    }

    @PutMapping("/objectives/{id}/archive")
    public SuccessResponse archiveObjective(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Archiving objective {} by user: {}", id, user.getUsername());
        try {
            Objective archivedObjective = objectiveService.archiveObjective(id, user);
            log.info("Objective archived successfully: {}", archivedObjective.getId());
            return new SuccessResponse(archivedObjective);
        } catch (Exception e) {
            log.error("Archive objective failed for id {} by user: {}", id, user.getUsername(), e);
            throw e;
        }
    }
}