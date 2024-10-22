package com.sparta.newsfeedproject.domain.member.dto;

import com.sparta.newsfeedproject.domain.member.entity.Follow;
import lombok.Getter;

@Getter
public class FollowResponseDto {
    private final Long id;
    private final Long followerMemberId;
    private final Long followedMemberId;

    public FollowResponseDto(Follow follow) {
        this.id = follow.getId();
        this.followerMemberId = follow.getFollowerMember().getId();
        this.followedMemberId = follow.getFollowedMember().getId();
    }
}
