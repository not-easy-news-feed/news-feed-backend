package com.sparta.newsfeedproject.domain.post.service;

import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.comment.repository.CommentRepository;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, Member member) {
        Post post = new Post(requestDto, member);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public Page<PostResponseDto> getPosts(Pageable pageable, Member member) {
        if (member == null) {
            throw new IllegalArgumentException("사용자가 인증되지 않았습니다.");
        }

        return postRepository.findAll(pageable).map(post -> {
            List<CommentResponseDto> comments = commentRepository.findByPostId(post.getId())
                    .stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
            return new PostResponseDto(post, comments);
        });
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, @Valid PostRequestDto requestDto, Member member) {
        Post post = postRepository.findByPostId(postId);

        // 작성자 검증 로직
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("게시물을 수정할 권한이 없습니다.");
        }

        post.updateData(requestDto);
        return post.to();
    }
}
