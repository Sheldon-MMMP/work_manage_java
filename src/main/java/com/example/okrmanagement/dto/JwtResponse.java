package com.example.okrmanagement.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String username;
    private String email;
    private String avatar;

    public JwtResponse(String accessToken, String username, String email, String avatar) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
    }
}