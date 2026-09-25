package com.roadways.login.config;

import com.roadways.login.security.JwtFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtFilter filter;

    public SecurityConfig(JwtFilter filter) {
        this.filter = filter;
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http.csrf(c -> c.disable()).formLogin(f -> f.disable()).httpBasic(b -> b.disable()).authorizeHttpRequests(a -> a.requestMatchers("/auth/login/user", "/swagger-ui/**", "/v3/api-docs/**").permitAll().anyRequest().authenticated()).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
