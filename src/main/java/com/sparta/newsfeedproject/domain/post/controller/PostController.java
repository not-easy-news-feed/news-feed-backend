package com.sparta.newsfeedproject.domain.post.controller;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    @PostMapping()
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.createPost(requestDto));
    }

    @PutMapping("{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostRequestDto requestDto,
            HttpServletRequest request
    ) {
//        Member member = (Member) request.getSession().getAttribute("member");

// 임시로 요청 바디에서 memberId를 받아 Member 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + requestDto.getMemberId()));

        PostResponseDto responseDto = postService.updatePost(postId, requestDto, member);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }
}


