package com.example.okrmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;

/**
 * 排除 Spring Security 自动配置：项目使用 Sa-Token 做认证，避免 Security 默认拦截并 302 重定向到 /login。
 * ManagementWebSecurityAutoConfiguration 依赖 HttpSecurity，主 Security 排除后也需一并排除。
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class
})
public class OkrManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(OkrManagementApplication.class, args);
    }

}