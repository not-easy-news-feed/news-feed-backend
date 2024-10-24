package com.sparta.newsfeedproject.domain.comment.entity;

import com.sparta.newsfeedproject.domain.common.TimeStamped;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String name;

    // 작성자와 댓글 = 다 대 일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    // 게시글과 댓글 = 다 대 일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(String content, Post post, Member member) {
        this.content = content;
        this.post = post;
        this.member = member;
        this.name = member.getName();
    }

    public void update(String content) {
        this.content = content;
    }
}
