package com.sparta.newsfeedproject.domain.post.service;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + requestDto.getMemberId()));
        Post post = postRepository.save(Post.from(requestDto, member));
        return post.to();
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
