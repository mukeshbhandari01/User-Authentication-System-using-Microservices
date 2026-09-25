package com.roadways.login.client;

import com.roadways.login.entity.AuthRequest;
import com.roadways.login.entity.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "REGISTER-SERVICE", url = "http://localhost:8082")
public interface RegisterClient {
    @GetMapping("/internal/users/{email}")
    UserResponse findByEmail(@PathVariable String email);

    @PostMapping("/internal/users/verify")
    UserResponse verify(@RequestBody AuthRequest request);
}
