package com.sparta.newsfeedproject.domain.comment.dto;

import com.sparta.newsfeedproject.domain.common.TimeStamped;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentRequestDto extends TimeStamped {
    private String content;
    private LocalDateTime updatedAt;
}
