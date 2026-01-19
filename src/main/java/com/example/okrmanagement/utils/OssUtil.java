package com.example.okrmanagement.utils;

import com.aliyun.oss.OSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Service
public class OssUtil {

    @Autowired
    private OSS ossClient;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    // 建议在 application.yml 中配置 bucket 名字，不要硬编码
    @Value("${alibaba.cloud.oss.bucket-name}")
    private String bucketName;

    /**
     * 获取受保护文件的临时访问 URL
     * @param fileName 在 OSS 上的完整路径名 (例如: "avatars/user01.jpg")
     * @param expirationHours 链接有效期（小时）
     * @return 带有签名的访问链接
     */
     public String getPresignedUrl(String fileName, int expirationHours) {
        // 设置有效期
        Date expiration = new Date(System.currentTimeMillis() + expirationHours * 3600 * 1000L);
        // 生成签名 URL
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
        return url.toString();
    }
}