package com.sparta.newsfeedproject.domain.member.dto;

import lombok.Getter;

@Getter
public class DeleteRequestDto {
    private String email;
    private String password;
}
