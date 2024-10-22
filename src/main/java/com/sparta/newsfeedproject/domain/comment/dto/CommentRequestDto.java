package com.sparta.newsfeedproject.domain.comment.dto;

import com.sparta.newsfeedproject.domain.common.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentRequestDto extends TimeStamped {
    private String content;
    private LocalDateTime updatedAt;
}
