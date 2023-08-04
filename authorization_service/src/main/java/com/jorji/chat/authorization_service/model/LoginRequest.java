package com.jorji.chat.authorization_service.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
