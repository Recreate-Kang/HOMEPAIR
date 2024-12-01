package com.home.ssafyhome.auth.model;

import lombok.Data;

@Data
public class User {

    private int userId;
    private String username;
    private String userIdValue;
    private String userpw;
    private String email;
    private String phoneNumber;
    private String role;

}
