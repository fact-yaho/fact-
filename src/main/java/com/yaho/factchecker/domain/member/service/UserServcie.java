package com.yaho.factchecker.domain.member.service;


import com.yaho.factchecker.domain.member.dto.request.LoginRequest;
import com.yaho.factchecker.domain.member.dto.request.SignUpRequest;
import com.yaho.factchecker.domain.member.entity.Role;
import com.yaho.factchecker.domain.member.entity.User;
import com.yaho.factchecker.domain.member.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional

public class UserServcie {

    private final UserRepository userRepository;
 /* 회원가입로직*/

    @Transactional
    public Long signUp(SignUpRequest request) {
        //  이메일 중복 가입 확인
        if( userRepository.existsByEmail(request.getEmail())){
            throw  new IllegalArgumentException("Email already exists");
        }
    // 새로운 유저 엔티티 생성 기본값 유저
    User user = new User(
            request.getEmail(),
            request.getPassword(),
            request.getName(),
            Role.USER
    );
        //포스트그레 저장
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

        // 회원 삭제 로직
    public void deleteUser(Long Userid){
        if( userRepository.existsById(Userid)){

            throw  new IllegalArgumentException("User already exists, 존재 하지않는 회원입니다"+Userid);

        }
        userRepository.deleteById(Userid);
    }
//  이메일 중복 체크
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
//  닉네임 중복 체크
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository .existsByNickname(nickname);
    }

// 로그인로직
    public Long login(LoginRequest request) {

        //이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: 이메일을 찾을수 없습니다 " + request.getEmail()));

        //비밀번호 확인
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid password: 비밀번호가 일치하지 않습니다");

        }
        // 로그인 성공시 유저ID 반환
        return user.getId();
    }



}
