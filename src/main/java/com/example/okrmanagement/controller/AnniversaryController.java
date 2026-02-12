package com.example.okrmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.common.TypeValidator;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.Anniversary;
import com.example.okrmanagement.service.AnniversaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SuccessResponse createAnniversary(@RequestBody Anniversary anniversary) {
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Creating anniversary by user: {}", userId);
        try {
            Anniversary newAnniversary = anniversaryService.createAnniversary(anniversary);
            log.info("Anniversary created successfully: {}", newAnniversary.getId());
            return new SuccessResponse(newAnniversary);
        } catch (Exception e) {
            log.error("Create anniversary failed for user: {}", userId, e);
            throw e;
        }
    }

    @GetMapping
    public SuccessResponse getAnniversaries() {
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Getting anniversaries for user: {}", userId);
        try {
            List<Anniversary> anniversaries = anniversaryService.getAnniversaries();
            log.info("Got {} anniversaries for user: {}", anniversaries.size(), userId);
            return new SuccessResponse(anniversaries);
        } catch (Exception e) {
            log.error("Get anniversaries failed for user: {}", userId, e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public SuccessResponse updateAnniversary(@PathVariable String id, @RequestBody Anniversary anniversary) {
        TypeValidator.validatePathParam("id", id, Long.class);
        Long parsedId = Long.parseLong(id);
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Updating anniversary {} by user: {}", parsedId, userId);
        try {
            Anniversary updatedAnniversary = anniversaryService.updateAnniversary(parsedId, anniversary);
            log.info("Anniversary updated successfully: {}", updatedAnniversary.getId());
            return new SuccessResponse(updatedAnniversary);
        } catch (Exception e) {
            log.error("Update anniversary failed for id {} by user: {}", parsedId, userId, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteAnniversary(@PathVariable String id) {
        TypeValidator.validatePathParam("id", id, Long.class);
        Long parsedId = Long.parseLong(id);
        long userId = StpUtil.getLoginIdAsLong();
        log.info("Deleting anniversary {} by user: {}", parsedId, userId);
        try {
            anniversaryService.deleteAnniversary(parsedId);
            log.info("Anniversary deleted successfully: {}", parsedId);
            return new SuccessResponse();
        } catch (Exception e) {
            log.error("Delete anniversary failed for id {} by user: {}", parsedId, userId, e);
            throw e;
        }
    }
}
