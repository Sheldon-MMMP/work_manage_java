package com.example.okrmanagement.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.okrmanagement.dto.SuccessResponse;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.OssService;
import com.example.okrmanagement.service.UserService;
import com.example.okrmanagement.utils.OssUtil;
import com.example.okrmanagement.common.VerificExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private OssService ossService;

    @Autowired
    private OssUtil ossUtil;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/avatar")
    public SuccessResponse uploadAvatar(@Valid @RequestParam("avatarFile") MultipartFile avatarFile, BindingResult bindingResult) {
        try{
            VerificExceptionHandler.handleVerificationException(bindingResult);   
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userService.getUserById(userId);
            log.info("Uploading avatar for user: {}", user.getId());
            
            String url = ossService.uploadFile(avatarFile, "avatars/");
            
            // 更新用户头像URL
            user.setAvatar(url);
            userService.updateUser(user);
            
            log.info("Avatar uploaded successfully for user: {}", user.getId());
            
            // 返回 {"code":200,"message":"success","data":{"avatar":url}}
            java.util.Map<String, String> data = new java.util.HashMap<>();
            data.put("avatar", ossUtil.getPresignedUrl(url, 1));
            return new SuccessResponse(data);
        }catch(Exception e){
            log.error("Failed to upload avatar: {}", e.getMessage());
            throw e;
        }
    }
    
    @GetMapping("/profile")
    public SuccessResponse getUserProfile() {
        try{
            Long userId = StpUtil.getLoginIdAsLong();
            log.info("Getting profile for user: {}", userId);
            
            User profile = userService.getUserById(userId);
            // 为头像URL添加签名
            if(profile.getAvatar()==null){
                profile.setAvatar("avatars/default.png");
            }
            profile.setAvatar(ossUtil.getPresignedUrl(profile.getAvatar(), 1));

            return new SuccessResponse(profile);
        }catch(Exception e){
            log.error("Failed to get user profile: {}", e.getMessage());
            throw e;
        }
    }
        
        @PutMapping("/profile")
        public SuccessResponse updateUserProfile(@Valid @RequestBody User updatedUser, BindingResult bindingResult) {
            try{
                VerificExceptionHandler.handleVerificationException(bindingResult);
                Long userId = StpUtil.getLoginIdAsLong();
                log.info("Updating profile for user: {}", userId);
                
            // 确保只能更新自己的信息
            updatedUser.setId(userId); 
            User profile = userService.updateUser(updatedUser);
            
            log.info("Profile updated successfully for user: {}", userId);
            // 为头像URL添加签名
            if(profile.getAvatar()==null){
                profile.setAvatar("avatars/default.png");
            }
            profile.setAvatar(ossUtil.getPresignedUrl(profile.getAvatar(), 1));;
            return new SuccessResponse(profile);
        }catch(Exception e){
            log.error("Failed to update user profile: {}", e.getMessage());
            throw e;
        }
    }
}
