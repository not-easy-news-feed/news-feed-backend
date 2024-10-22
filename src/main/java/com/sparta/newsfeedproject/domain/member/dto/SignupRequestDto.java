package com.sparta.newsfeedproject.domain.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String name;
    private String password;
    private String email;
    private boolean admin = false;
    private String adminToken = "";
}