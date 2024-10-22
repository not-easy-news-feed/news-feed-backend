package com.sparta.newsfeedproject.domain.member.dto;

import lombok.Getter;

@Getter
public class FollowRequestDto {
    private Long memberId;
    private Long followedMemberId;
}
