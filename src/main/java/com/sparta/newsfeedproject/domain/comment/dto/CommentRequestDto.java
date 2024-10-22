package com.sparta.newsfeedproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String content;
    private Long memberId;
}
