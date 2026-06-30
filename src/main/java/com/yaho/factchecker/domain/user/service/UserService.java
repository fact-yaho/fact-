package com.yaho.factchecker.domain.user.service;

import com.yaho.factchecker.domain.user.dto.request.LoginRequest;
import com.yaho.factchecker.domain.user.dto.request.SignUpRequest;
import com.yaho.factchecker.domain.user.dto.response.MyPageResponse; // ✨ 추가
import com.yaho.factchecker.domain.user.entity.Role;
import com.yaho.factchecker.domain.user.entity.User;
import com.yaho.factchecker.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /* 1. 회원가입 로직 */
    @Transactional
    public Long signUp(SignUpRequest request) {
        // 이메일 중복 가입 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        // 새로운 유저 엔티티 생성 기본값 유저
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                Role.USER
        );
        // 포스트그레 저장
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    /* 2. 회원 삭제 로직 */
    @Transactional
    public void deleteUser(String email) {
        // 1. 이메일로 유저가 존재하는지 먼저 확인 겸 엔티티 가져오기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. 이메일: " + email));

        // 2. 찾아온 유저의 진짜 고유 ID로 확실하게 삭제 진행
        userRepository.deleteById(user.getId());
    }

    /* 3. 이메일 중복 체크 */
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    /* 4. 닉네임 중복 체크 */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /* 5. 로그인 로직 */
    public Long login(LoginRequest request) {
        // 이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: 이메일을 찾을수 없습니다 " + request.getEmail()));

        // 비밀번호 확인
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid password: 비밀번호가 일치하지 않습니다");
        }
        // 로그인 성공시 유저ID 반환
        return user.getId();
    }

    /* 6. 마이페이지 조회 로직 ✨ (새로 추가됨) */
    public MyPageResponse getMyPage(Long userId) {
        // 유저가 진짜 있는지 조회하고 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + userId));

        // 엔티티를  반환
        return new MyPageResponse(user);
    }
}