package com.sparta.newsfeedproject.domain.member.dto;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberWithPostsResponseDto {
    private final Long id;
    private final String email;
    private final String name;
    // private final List<PostResponseDto> postResponseDtoList;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MemberWithPostsResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        // this.postResponseDtoList = member.getPosts().stream().map(PostResponseDto::new).toList();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}
