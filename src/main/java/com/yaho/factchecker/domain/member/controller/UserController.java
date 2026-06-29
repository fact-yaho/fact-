package com.yaho.factchecker.domain.member.controller;


import com.yaho.factchecker.domain.member.dto.request.SignUpRequest;
import com.yaho.factchecker.domain.member.service.UserServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor


public class UserController {

    private final UserServcie userServcie;

    //회원가입 api
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request){
        Long userId = userServcie.signUp(request);
        return ResponseEntity.ok("회원가입  완료" + userId);

    }


    // 2. 회원탈퇴 API (Delete)
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam(name = "userId") Long userId) {
        // 실제 일은 서비스 인스턴스(userServcie)에게 시킵니다.
        userServcie.deleteUser(userId);
        return ResponseEntity.ok("회원탈퇴 완료. 유저 ID: " + userId);
    }

}
