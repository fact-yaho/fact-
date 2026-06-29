package com.yaho.factchecker.domain.member.controller;


import com.yaho.factchecker.domain.member.dto.request.SignUpRequest;
import com.yaho.factchecker.domain.member.service.MemberServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor


public class MemberController {

    private final MemberServcie memberServcie;

    //회원가입 api
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request){
        Long userId =memberServcie.signUp(request);
        return ResponseEntity.ok("회원가입  완료" + userId);

    }



}
