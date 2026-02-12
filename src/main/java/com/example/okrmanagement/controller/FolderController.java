package com.example.okrmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.Folder;
import com.example.okrmanagement.service.FolderService;
import com.example.okrmanagement.common.VerificExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    /**
     * 创建文件夹
     */
    @PostMapping
    public SuccessResponse createFolder(@Valid @RequestBody Folder folder, BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Long userId = StpUtil.getLoginIdAsLong();
            folder.setUserId(userId);
            log.info("Creating folder for user: {}", userId);

            Folder createdFolder = folderService.createFolder(folder);
            log.info("Folder created successfully: {}", createdFolder.getId());

            return new SuccessResponse(createdFolder);
        } catch (Exception e) {
            log.error("Failed to create folder: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 更新文件夹
     */
    @PutMapping("/{folderId}")
    public SuccessResponse updateFolder(@Valid @RequestBody Folder folder, @PathVariable Long folderId, BindingResult bindingResult) {
        try {
            VerificExceptionHandler.handleVerificationException(bindingResult);
            Long userId = StpUtil.getLoginIdAsLong();
            folder.setId(folderId);
            folder.setUserId(userId);
            log.info("Updating folder: {}", folderId);

            Folder updatedFolder = folderService.updateFolder(folder);
            log.info("Folder updated successfully: {}", updatedFolder.getId());

            return new SuccessResponse(updatedFolder);
        } catch (Exception e) {
            log.error("Failed to update folder: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 删除文件夹
     */
    @DeleteMapping("/{folderId}")
    public SuccessResponse deleteFolder(@PathVariable Long folderId) {
        try {
            log.info("Deleting folder: {}", folderId);
            folderService.deleteFolder(folderId);
            log.info("Folder deleted successfully: {}", folderId);

            return new SuccessResponse(null);
        } catch (Exception e) {
            log.error("Failed to delete folder: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取文件夹详情
     */
    @GetMapping("/{folderId}")
    public SuccessResponse getFolder(@PathVariable Long folderId) {
        try {
            log.info("Getting folder: {}", folderId);
            Folder folder = folderService.getFolderById(folderId);
            log.info("Folder retrieved successfully: {}", folder.getId());

            return new SuccessResponse(folder);
        } catch (Exception e) {
            log.error("Failed to get folder: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户的所有文件夹
     */
    @GetMapping
    public SuccessResponse getFolders() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            log.info("Getting folders for user: {}", userId);
            List<Folder> folders = folderService.getFoldersByUserId(userId);
            log.info("Retrieved {} folders for user: {}", folders.size(), userId);

            return new SuccessResponse(folders);
        } catch (Exception e) {
            log.error("Failed to get folders: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 获取指定父文件夹下的子文件夹
     */
    @GetMapping("/parent/{parentId}")
    public SuccessResponse getFoldersByParent(@PathVariable Long parentId) {
        try {
            log.info("Getting folders by parent: {}", parentId);
            List<Folder> folders = folderService.getFoldersByParentId(parentId);
            log.info("Retrieved {} folders for parent: {}", folders.size(), parentId);

            return new SuccessResponse(folders);
        } catch (Exception e) {
            log.error("Failed to get folders by parent: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 移动文件夹
     */
    @PutMapping("/{folderId}/move")
    public SuccessResponse moveFolder(@PathVariable Long folderId, @RequestParam(required = false) Long newParentId) {
        try {
            log.info("Moving folder: {} to parent: {}", folderId, newParentId);
            folderService.moveFolder(folderId, newParentId);
            log.info("Folder moved successfully: {}", folderId);

            return new SuccessResponse(null);
        } catch (Exception e) {
            log.error("Failed to move folder: {}", e.getMessage());
            throw e;
        }
    }
}
