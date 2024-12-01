package com.home.ssafyhome.auth.dto;

import lombok.Data;
@Data
public class SignupRequest {
    private String username;
    private String userId;
    private String password;
    private String email;
    private String phone_number; // JSON 키와 동일하게 수정
}