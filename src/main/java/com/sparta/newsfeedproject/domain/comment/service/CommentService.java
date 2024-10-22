package com.sparta.newsfeedproject.domain.comment.service;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.comment.entity.Comment;
import com.sparta.newsfeedproject.domain.comment.repository.CommentRepository;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Member member) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = new Comment(requestDto.getContent(), member, post);
        commentRepository.save(comment); //생성 및 저장

        return new CommentResponseDto(comment.getId(), comment.getContent(),comment.getUsername(),comment.getCreatedAt(),comment.getUpdatedAt());
    }
}
