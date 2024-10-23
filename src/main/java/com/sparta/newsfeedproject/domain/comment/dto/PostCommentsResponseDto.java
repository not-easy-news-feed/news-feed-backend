package com.sparta.newsfeedproject.domain.comment.dto;

import com.sparta.newsfeedproject.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCommentsResponseDto {
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponseDto> comments;

    public PostCommentsResponseDto(Post post, List<CommentResponseDto> comments) {
        this.author = post.getMember().getName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.comments = comments;
    }
}
