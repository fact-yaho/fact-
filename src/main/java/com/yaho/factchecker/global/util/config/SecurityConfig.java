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
                        // 회원가입 주소는 인증 없이 누구나 들어올 수 있도록 완전히 열어줍니다!
                        // (컨트롤러의 오타 버전 /singup과 정석 /signup 둘 다 등록해 둘게요)
                        .requestMatchers("/api/v1/members/signup").permitAll()

                        // 그 외의 나머지 모든 API는 로그인이 필요하도록 막아둡니다.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}