package com.home.ssafyhome.auth.dto;

import lombok.Data;

@Data
public class JwtTokenDTO {
    String jwtToken;
    String userName;
    String userEmail;
    String userPhone;

    public static JwtTokenDTO of(String serialzied) {
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setJwtToken(serialzied);
        return jwtTokenDTO;
    }
}