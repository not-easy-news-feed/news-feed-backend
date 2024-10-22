package com.sparta.newsfeedproject.domain.post.entity;

import com.sparta.newsfeedproject.domain.comment.entity.Comment;
import com.sparta.newsfeedproject.domain.common.TimeStamped;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;
    
    // 양방향 관계 설정
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    private void initData(PostRequestDto requestDto, Member member) {
        this.member = member;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public static Post from(PostRequestDto requestDto, Member member) {
        Post post = new Post();
        post.initData(requestDto, member);
        return post;
    }

    public PostResponseDto to() {
        return new PostResponseDto(
                id,
                this.member.getEmail(),
                title,
                content,
                comments.stream().map(Comment::to).toList(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}
