package com.sparta.newsfeedproject.domain.comment.entity;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.common.TimeStamped;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // 작성자와 댓글 = 다 대 일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    // 게시글과 댓글 = 다 대 일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    // 메서드 추가
    public static Comment from(CommentRequestDto requestDto, Post post, Member member) {
        Comment comment = new Comment();
        comment.initData(requestDto, post, member);
        return comment;
    }

    private void initData(CommentRequestDto requestDto, Post post, Member member) {
        this.content = requestDto.getContent();
        this.member = member;
        this.post = post;
    }

    public CommentResponseDto to() {
        return new CommentResponseDto(
                this.id,
                this.content,
                this.member.getEmail()
        );
    }
}
