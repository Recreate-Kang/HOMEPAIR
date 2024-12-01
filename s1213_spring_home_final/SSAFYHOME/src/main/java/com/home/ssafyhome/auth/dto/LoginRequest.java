package com.home.ssafyhome.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String userId;
    String password;
}
