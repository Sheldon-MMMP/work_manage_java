
package com.example.okrmanagement.service;

import com.aliyun.oss.OSS;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.io.IOException;

@Service
public class OssService {

    @Autowired
    private OSS ossClient; // 自动注入

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Value("${alibaba.cloud.oss.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file,String folder) {
        // 验证文件是否为null
        if (file == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorCode.INVALID_AVATAR_TYPE);
        }
        // 生成唯一文件名
        String fileName = folder + UUID.randomUUID() + "-" + file.getOriginalFilename();

        // 上传文件
        try {
            ossClient.putObject(bucketName, fileName, file.getInputStream());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return fileName;
    }

}