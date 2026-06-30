package com.yaho.factchecker.domain.user.dto.request;

public class SignUpRequest {

    private String email;
    private String password;
    private String name;




    //기본 생성자
    public SignUpRequest() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}

