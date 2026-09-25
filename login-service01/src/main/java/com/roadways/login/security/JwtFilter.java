package com.roadways.login.security;

import com.roadways.login.service.RedisService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final RedisService redis;

    public JwtFilter(JwtService jwt, RedisService redis) {
        this.jwt = jwt;
        this.redis = redis;
    }

    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String t = token(req);
        if (t != null && !redis.blacklisted(t) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String email = jwt.email(t);
                String role = jwt.role(t);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))));
                req.setAttribute("jwt", t);
                req.setAttribute("email", email);
            } catch (Exception ignored) {
            }
        }
        chain.doFilter(req, res);
    }

    private String token(HttpServletRequest r) {
        Cookie[] c = r.getCookies();
        if (c != null) for (Cookie x : c) if ("jwt".equals(x.getName())) return x.getValue();
        return null;
    }
}
