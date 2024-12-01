package com.home.ssafyhome.auth.controller;

import com.home.ssafyhome.auth.dto.JwtTokenDTO;
import com.home.ssafyhome.auth.dto.LoginRequest;
import com.home.ssafyhome.auth.dto.SignupRequest;
import com.home.ssafyhome.auth.model.User;
import com.home.ssafyhome.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<JwtTokenDTO> login(@RequestBody LoginRequest logindata) {
        return ResponseEntity.ok().body(authService.login(logindata));
    }
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signuprequest) {
        System.out.println("Received signup request: ");
        authService.signup(signuprequest);
        return ResponseEntity.ok().body("Signup successful");
    }
}
