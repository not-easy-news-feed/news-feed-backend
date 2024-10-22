package com.sparta.newsfeedproject.domain.post.controller;

import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 게시물 작성하기
    @PostMapping()
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto reequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.createPost(reequestDto));
    }
}
