package com.campus.lostfound.controller;

import com.campus.lostfound.dto.AuthDtos;
import com.campus.lostfound.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public AuthDtos.AuthResponse register(@RequestBody AuthDtos.RegisterRequest req) {
        return auth.register(req);
    }

    @PostMapping("/login")
    public AuthDtos.AuthResponse login(@RequestBody AuthDtos.LoginRequest req) {
        return auth.login(req);
    }
}
