package com.yaho.factchecker.domain.member.controller;


import com.yaho.factchecker.domain.member.dto.request.SignUpRequest;
import com.yaho.factchecker.domain.member.dto.response.MyPageResponse;
import com.yaho.factchecker.domain.member.service.UserServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
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

        userServcie.deleteUser(userId);
        return ResponseEntity.ok("회원탈퇴 완료. 유저 ID: " + userId);
    }
    // 3.이메일 중복체크
    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email){

    String cleanEmail =email.replace("\"", "")
            .trim(); // 공백 제거

        boolean isDuplicate = userServcie.checkEmailDuplicate(cleanEmail);
        if(isDuplicate){
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }
    // 4.닉네임 중복 체크 api
    @PostMapping("/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestParam(name = "nickname") String nickname) {
        String cleanNickname = nickname.replace("\"", "").trim(); // 공백 제거
        boolean isDuplicate = userServcie.checkNicknameDuplicate(cleanNickname);
        if (isDuplicate) {
            return ResponseEntity.badRequest().body("이미 존재하는 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    //5. 마이페이지 api 추가
    @PostMapping("/me")
    public ResponseEntity<MyPageResponse>getMyPage(@RequestParam(name = "userId") Long userId) {

        MyPageResponse response = userServcie.getMyPage(userId);

        return ResponseEntity.ok(response);
    }

}
