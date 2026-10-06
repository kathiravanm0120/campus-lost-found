package com.campus.lostfound.service;

import com.campus.lostfound.dto.AuthDtos;
import com.campus.lostfound.entity.Role;
import com.campus.lostfound.entity.User;
import com.campus.lostfound.repository.UserRepository;
import com.campus.lostfound.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req) {
        if (users.findByEmailIgnoreCase(req.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email().trim().toLowerCase());
        user.setPassword(encoder.encode(req.password()));
        user.setRole(Role.STUDENT);
        users.save(user);
        return response(user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        User user = users.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return response(user);
    }

    private AuthDtos.AuthResponse response(User user) {
        return new AuthDtos.AuthResponse(
                jwt.generate(user.getId(), user.getEmail(), user.getRole().name()),
                user.getId(), user.getName(), user.getEmail(), user.getRole().name()
        );
    }
}
