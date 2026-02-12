package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Folder;
import java.util.List;

public interface FolderService {
    /**
     * 创建文件夹
     */
    Folder createFolder(Folder folder);

    /**
     * 更新文件夹
     */
    Folder updateFolder(Folder folder);

    /**
     * 删除文件夹
     */
    void deleteFolder(Long folderId);

    /**
     * 获取文件夹详情
     */
    Folder getFolderById(Long folderId);

    /**
     * 获取用户的所有文件夹
     */
    List<Folder> getFoldersByUserId(Long userId);

    /**
     * 获取指定父文件夹下的子文件夹
     */
    List<Folder> getFoldersByParentId(Long parentId);

    /**
     * 移动文件夹到新的父文件夹
     */
    void moveFolder(Long folderId, Long newParentId);
}
