package com.library.management.dto;

import com.library.management.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private UserRole role;


    public LoginResponse(String accessToken, Long userId, String username, String email, UserRole role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}