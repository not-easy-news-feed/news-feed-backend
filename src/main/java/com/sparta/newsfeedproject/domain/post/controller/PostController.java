package com.sparta.newsfeedproject.domain.post.controller;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
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

    @PostMapping()
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody @Valid PostRequestDto requestDto,
            HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        PostResponseDto responseDto = postService.createPost(requestDto, member);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostRequestDto requestDto,
            HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");

        PostResponseDto responseDto = postService.updatePost(postId, requestDto, member);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             HttpServletRequest request) {

        Member member = (Member) request.getAttribute("member");
        postService.deletePost(postId, member);

        return ResponseEntity.status(HttpStatus.OK)
                .body("게시물이 성공적으로 삭제되었습니다.");
    }
}