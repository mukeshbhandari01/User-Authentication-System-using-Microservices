package com.roadways.login.entity;

import com.roadways.login.entity.Role;

public record UserResponse(Long id, String username, String email, Role role) {
}
