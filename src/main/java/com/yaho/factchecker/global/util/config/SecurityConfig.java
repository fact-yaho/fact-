package com.yaho.factchecker.global.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 일단끄기
                .csrf(csrf -> csrf.disable())

                //
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/v1/users/signup").permitAll()//회원가입
                        .requestMatchers("/api/v1/users/check-email").permitAll()//이메일체크
                        .requestMatchers("/api/v1/users/check-nickname").permitAll()//닉네임체크
                        .requestMatchers("/api/v1/users/{userId}").permitAll()//삭제

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}