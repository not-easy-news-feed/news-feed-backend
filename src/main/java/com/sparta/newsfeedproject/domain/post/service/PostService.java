package com.sparta.newsfeedproject.domain.post.service;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, Member member) {
        Post post = new Post(requestDto, member);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 작성자 검증
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다. 게시물을 수정할 권한이 없습니다.");
        }

        post.updateData(requestDto);
        postRepository.saveAndFlush(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다. 게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.deleteById(postId);
    }
}