package com.sparta.newsfeedproject.domain.member.dto;

import lombok.Getter;

@Getter
public class UpdateRequestDto {

    private String password;
    private String updatedName;
    private String updatedPassword;
}
