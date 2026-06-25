package com.yaho.factchecker.domain.member.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {

    private String Email;
    private String Password;
    private String name;


}
