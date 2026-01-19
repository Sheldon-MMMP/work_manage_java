package com.example.okrmanagement.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Value("${alibaba.cloud.access-key}")
    private String accessKey;

    @Value("${alibaba.cloud.secret-key}")
    private String secretKey;

    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKey, secretKey);
    }
}