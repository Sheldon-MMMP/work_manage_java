package com.example.okrmanagement.controller;

import com.example.okrmanagement.common.TypeValidator;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.Anniversary;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.AnniversaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/anniversaries")
public class AnniversaryController {
    @Autowired
    private AnniversaryService anniversaryService;

    @PostMapping
    public SuccessResponse createAnniversary(@RequestBody Anniversary anniversary, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Creating anniversary by user: {}", user.getUsername());
        try {
            Anniversary newAnniversary = anniversaryService.createAnniversary(anniversary, user);
            log.info("Anniversary created successfully: {}", newAnniversary.getId());
            return new SuccessResponse(newAnniversary);
        } catch (Exception e) {
            log.error("Create anniversary failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @GetMapping
    public SuccessResponse getAnniversaries(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Getting anniversaries for user: {}", user.getUsername());
        try {
            List<Anniversary> anniversaries = anniversaryService.getAnniversaries(user);
            log.info("Got {} anniversaries for user: {}", anniversaries.size(), user.getUsername());
            return new SuccessResponse(anniversaries);
        } catch (Exception e) {
            log.error("Get anniversaries failed for user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public SuccessResponse updateAnniversary(@PathVariable String id, @RequestBody Anniversary anniversary, Authentication authentication) {
        TypeValidator.validatePathParam("id", id, Long.class);
        Long parsedId = Long.parseLong(id);
        User user = (User) authentication.getPrincipal();
        log.info("Updating anniversary {} by user: {}", parsedId, user.getUsername());
        try {
            Anniversary updatedAnniversary = anniversaryService.updateAnniversary(parsedId, anniversary, user);
            log.info("Anniversary updated successfully: {}", updatedAnniversary.getId());
            return new SuccessResponse(updatedAnniversary);
        } catch (Exception e) {
            log.error("Update anniversary failed for id {} by user: {}", parsedId, user.getUsername(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteAnniversary(@PathVariable String id, Authentication authentication) {
        TypeValidator.validatePathParam("id", id, Long.class);
        Long parsedId = Long.parseLong(id);
        User user = (User) authentication.getPrincipal();
        log.info("Deleting anniversary {} by user: {}", parsedId, user.getUsername());
        try {
            anniversaryService.deleteAnniversary(parsedId, user);
            log.info("Anniversary deleted successfully: {}", parsedId);
            return new SuccessResponse();
        } catch (Exception e) {
            log.error("Delete anniversary failed for id {} by user: {}", parsedId, user.getUsername(), e);
            throw e;
        }
    }
}