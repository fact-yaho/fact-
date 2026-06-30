package com.yaho.factchecker.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;

    // 테스트나 생성용 인터페이스 (필요시)
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}