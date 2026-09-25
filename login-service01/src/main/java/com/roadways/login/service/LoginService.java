package com.roadways.login.service;

import com.roadways.login.client.RegisterClient;
import com.roadways.login.entity.*;
import com.roadways.login.exception.ApiException;
import com.roadways.login.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginService {
    private final RegisterClient registerClient;
    private final JwtService jwt;
    private final RedisService redis;

    public LoginService(RegisterClient registerClient, JwtService jwt, RedisService redis) {
        this.registerClient = registerClient;
        this.jwt = jwt;
        this.redis = redis;
    }

    public String login(AuthRequest req, Role expectedRole) {
        System.out.println(req.getEmail()+"hello1");
        if (req == null || req.getEmail() == null || req.getEmail().isBlank() || req.getPassword() == null || req.getPassword().isBlank())
            throw new ApiException("Email and password are required");
        UserResponse u;
        try {
            u = registerClient.verify(req);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("Invalid email or password");
        }
        if (expectedRole != null && u.role() != expectedRole)
            throw new ApiException("This account does not have the required role");
        return jwt.generate(u.email(), u.role());
    }

    public UserResponse me(String email) {
        try {
            return registerClient.findByEmail(email);
        } catch (Exception e) {
            throw new ApiException("User not found");
        }
    }

    public void logout(String token) {
        if (token != null && !token.isBlank()) redis.blacklist(token, Duration.ofMillis(jwt.remaining(token)));
    }
}
