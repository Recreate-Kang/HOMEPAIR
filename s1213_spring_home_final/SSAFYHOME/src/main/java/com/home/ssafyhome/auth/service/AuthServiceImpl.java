package com.home.ssafyhome.auth.service;

import com.home.ssafyhome.auth.dto.JwtTokenDTO;
import com.home.ssafyhome.auth.dto.LoginRequest;
import com.home.ssafyhome.auth.dto.SignupRequest;
import com.home.ssafyhome.auth.mapper.AuthMapper;
import com.home.ssafyhome.auth.model.User;
import com.home.ssafyhome.auth.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    AuthMapper authMapper;

    AuthServiceImpl(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Override
    public JwtTokenDTO login(LoginRequest logindata) {
        User user = authMapper.findByUserIdValueAndUserpw(logindata.getUserId(), logindata.getPassword());
        JwtTokenDTO jwtTokenDTO = null;
        if (user != null) {
            jwtTokenDTO = JwtUtil.createJwtTokenDTO(user);
        }
        jwtTokenDTO.setUserName(user.getUsername());
        jwtTokenDTO.setUserEmail(user.getEmail());
        jwtTokenDTO.setUserPhone(user.getPhoneNumber());
        return jwtTokenDTO;
    }
    @Override
    public void signup(SignupRequest signupRequest) {
        // SignupRequest -> User 변환
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setUserIdValue(signupRequest.getUserId());
        user.setUserpw(signupRequest.getPassword());
        user.setEmail(signupRequest.getEmail());
        user.setPhoneNumber(signupRequest.getPhone_number());
        user.setRole("user"); // 기본값 설정

        // 사용자 저장
        authMapper.insertUser(user);
    }
}
