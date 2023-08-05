package com.jorji.chat.usermanagementservice.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private boolean prv;
}
