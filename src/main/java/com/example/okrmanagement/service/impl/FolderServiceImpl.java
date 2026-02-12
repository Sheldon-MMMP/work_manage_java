package com.example.okrmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.okrmanagement.entity.Folder;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.mapper.FolderMapper;
import com.example.okrmanagement.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {

    @Override
    public Folder createFolder(Folder folder) {
        if (folder.getUserId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        if (folder.getName() == null || folder.getName().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        List<Folder> existingFolders = lambdaQuery()
                .eq(Folder::getUserId, folder.getUserId())
                .eq(Folder::getParentId, folder.getParentId())
                .eq(Folder::getName, folder.getName())
                .list();
        if (!existingFolders.isEmpty()) {
            throw new BusinessException(ErrorCode.FOLDER_NAME_EXISTS);
        }
        save(folder);
        log.info("Folder created successfully: {}", folder.getId());
        return folder;
    }

    @Override
    public Folder updateFolder(Folder folder) {
        if (folder.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        Folder existingFolder = getFolderById(folder.getId());
        if (folder.getName() != null) {
            existingFolder.setName(folder.getName());
        }
        if (folder.getParentId() != null) {
            existingFolder.setParentId(folder.getParentId());
        }
        updateById(existingFolder);
        log.info("Folder updated successfully: {}", existingFolder.getId());
        return existingFolder;
    }

    @Override
    public void deleteFolder(Long folderId) {
        if (folderId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        getFolderById(folderId);
        List<Folder> subFolders = lambdaQuery().eq(Folder::getParentId, folderId).list();
        if (!subFolders.isEmpty()) {
            throw new BusinessException(ErrorCode.FOLDER_HAS_CHILDREN);
        }
        getBaseMapper().deleteById(folderId);
        log.info("Folder deleted successfully: {}", folderId);
    }

    @Override
    public Folder getFolderById(Long folderId) {
        Folder folder = getById(folderId);
        if (folder == null) {
            throw new BusinessException(ErrorCode.FOLDER_NOT_FOUND);
        }
        return folder;
    }

    @Override
    public List<Folder> getFoldersByUserId(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return lambdaQuery().eq(Folder::getUserId, userId).list();
    }

    @Override
    public List<Folder> getFoldersByParentId(Long parentId) {
        return lambdaQuery().eq(Folder::getParentId, parentId).list();
    }

    @Override
    public void moveFolder(Long folderId, Long newParentId) {
        if (folderId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        Folder folder = getFolderById(folderId);
        if (newParentId != null) {
            getFolderById(newParentId);
            if (isAncestor(newParentId, folderId)) {
                throw new BusinessException(ErrorCode.FOLDER_CIRCULAR_DEPENDENCY);
            }
        }
        folder.setParentId(newParentId);
        updateById(folder);
        log.info("Folder moved successfully: {} to parent {}", folderId, newParentId);
    }

    private boolean isAncestor(Long targetFolderId, Long sourceFolderId) {
        Folder folder = getById(sourceFolderId);
        if (folder == null || folder.getParentId() == null) {
            return false;
        }
        if (folder.getParentId().equals(targetFolderId)) {
            return true;
        }
        return isAncestor(targetFolderId, folder.getParentId());
    }
}
