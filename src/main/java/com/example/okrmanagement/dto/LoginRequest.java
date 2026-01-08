package com.example.okrmanagement.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotBlank(message = "email不能为空")
    @Email(message = "email格式错误")
    private String email;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少为6个字符")
    private String password;
}