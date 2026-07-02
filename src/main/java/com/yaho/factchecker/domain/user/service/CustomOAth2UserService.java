package com.yaho.factchecker.domain.user.service;

import com.yaho.factchecker.domain.user.entity.User;
import com.yaho.factchecker.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAth2UserService implements OAuth2UserService <OAuth2UserRequest, OAuth2User>{

    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)  throws OAuth2AuthenticationException {

       OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
       OAuth2User oAuth2User = delegate.loadUser(userRequest);

       String registrationId = userRequest.getClientRegistration().getRegistrationId();
       String userName = userRequest.getClientRegistration()
               .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

       Map<String,Object> attributes = oAuth2User.getAttributes();

       String email = (String) attributes.get("email");
       String name = (String) attributes.get("name");

       log.info("CustomOAth2UserService loadUser 호출");

       User user = saveOrUpate(email, name);

       return  new DefaultOAuth2User(
               Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
               attributes,
               userName
       );
    }

    private User saveOrUpate(String email, String nickname){

        return userRepository.findByEmail(email)
                .map(entity->{
                    return entity;
                })
                .orElseGet(()->{
                    User newUser =User.builder()
                            .email(email)
                            .nickname(nickname) // 구글 이름을 기본 닉네임으로 설정 예시
                            .password("")
                            .build();
                    return userRepository.save(newUser);
                });
    }




}
