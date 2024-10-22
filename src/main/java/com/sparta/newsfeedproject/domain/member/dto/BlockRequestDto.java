package com.sparta.newsfeedproject.domain.member.dto;

import lombok.Getter;

@Getter
public class BlockRequestDto {
    private Long memberId;
    private Long blockedId;
}
