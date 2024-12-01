package com.home.ssafyhome.auth.service;

import com.home.ssafyhome.auth.dto.JwtTokenDTO;
import com.home.ssafyhome.auth.dto.LoginRequest;
import com.home.ssafyhome.auth.dto.SignupRequest;
import com.home.ssafyhome.auth.model.User;

public interface AuthService {
    JwtTokenDTO login(LoginRequest logindata);

	void signup(SignupRequest signupRequest);
    

}
