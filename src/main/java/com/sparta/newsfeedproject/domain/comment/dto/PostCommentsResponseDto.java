package com.sparta.newsfeedproject.domain.comment.dto;

import com.sparta.newsfeedproject.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCommentsResponseDto {
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponseDto> commentResponseDtoList;

    public PostCommentsResponseDto(Post post) {
        this.author = post.getMember().getName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.commentResponseDtoList = post.getComments().stream().map(CommentResponseDto::new).toList();
    }
}
