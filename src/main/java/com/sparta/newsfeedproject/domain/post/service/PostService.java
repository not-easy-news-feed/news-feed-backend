package com.sparta.newsfeedproject.domain.post.service;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
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
}
