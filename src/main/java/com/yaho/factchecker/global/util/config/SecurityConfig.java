package com.yaho.factchecker.global.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))

                // api 접근권한
                .authorizeHttpRequests(auth -> auth

                        // 회원가입과 닉네임    중복체크는 인증없이 접근 가능하도록 설정
                        .requestMatchers("/api/v1/users/signup","/api/v1/users/check-nickname").permitAll()
                        //실제 로그인
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        //그외 다른것들은 토큰필요
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }


    //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //RestTemplate Bean 생성
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}