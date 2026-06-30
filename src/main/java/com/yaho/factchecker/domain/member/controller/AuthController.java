package com.yaho.factchecker.domain.member.controller;

import com.yaho.factchecker.domain.member.dto.request.LoginRequest;
import com.yaho.factchecker.domain.member.service.UserServcie;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServcie userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){

        Long userId =userService.login(request);

        return ResponseEntity.ok("로그인 성공" + userId);

    }

}
