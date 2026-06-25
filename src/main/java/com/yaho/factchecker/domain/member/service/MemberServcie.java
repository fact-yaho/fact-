package com.yaho.factchecker.domain.member.service;


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

public class MemberServcie {

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

        //포스그레 저장
        User savedUser = userRepository.save(user);
        return savedUser.getId();

    }
}
