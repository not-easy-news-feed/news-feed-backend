package com.sparta.newsfeedproject.domain.post.dto;

import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<CommentResponseDto> comments;
    private MemberResponseDto member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.member = new MemberResponseDto(post.getMember());
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
