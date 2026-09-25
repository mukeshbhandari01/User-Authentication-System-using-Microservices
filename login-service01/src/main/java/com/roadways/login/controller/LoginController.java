package com.roadways.login.controller;

import com.roadways.login.entity.*;
import com.roadways.login.service.LoginService;
import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth/login")
public class LoginController {
    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/user")
    public ResponseEntity<?> user(@RequestBody AuthRequest r, HttpServletResponse res) {
        System.out.println(r.getEmail()+"hello");
        return login(r, Role.USER, res);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest req) {
        return ResponseEntity.ok(service.me((String) req.getAttribute("email")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        service.logout((String) req.getAttribute("jwt"));
        clearCookie(res);
        return ResponseEntity.ok("Logout successful");
    }

    private ResponseEntity<?> login(AuthRequest r, Role role, HttpServletResponse res) {
        String token = service.login(r, role);
        writeCookie(token, res);
        return ResponseEntity.ok("Login successful");
    }

    private void writeCookie(String token, HttpServletResponse res) {
        ResponseCookie c = ResponseCookie.from("jwt", token).httpOnly(true).secure(false).sameSite("Lax").path("/").maxAge(Duration.ofHours(1)).build();
        res.addHeader(HttpHeaders.SET_COOKIE, c.toString());
    }

    private void clearCookie(HttpServletResponse res) {
        ResponseCookie c = ResponseCookie.from("jwt", "").httpOnly(true).secure(false).sameSite("Lax").path("/").maxAge(Duration.ZERO).build();
        res.addHeader(HttpHeaders.SET_COOKIE, c.toString());
    }
}
