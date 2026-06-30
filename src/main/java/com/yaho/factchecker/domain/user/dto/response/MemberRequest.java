package com.yaho.factchecker.domain.user.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {

    private String Email;
    private String Password;
    private String name;


}
