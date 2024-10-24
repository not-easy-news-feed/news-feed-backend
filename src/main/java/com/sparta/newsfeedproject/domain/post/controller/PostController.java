package com.sparta.newsfeedproject.domain.post.controller;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody @Valid PostRequestDto requestDto,
            HttpServletRequest request
    ) {
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
            HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");

        PostResponseDto responseDto = postService.updatePost(postId, requestDto, member);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");
        postService.deletePost(postId, member);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("게시물이 성공적으로 삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostResponseDto> postsResponseDto = postService.getPosts(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postsResponseDto);
    }

    @GetMapping("/friend")
    public ResponseEntity<Page<PostResponseDto>> getFriendPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostResponseDto> postsResponseDto = postService.getFriendPosts(member.getId(), pageable);

        return new ResponseEntity<>(postsResponseDto, HttpStatus.OK);
    }

    // 기간별 게시물 조회
    @GetMapping("/date-range")
    public ResponseEntity<Page<PostResponseDto>> getPostsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<PostResponseDto> postsResponseDto = postService.getPostsByDateRange(startDate, endDate, pageable);

        return new ResponseEntity<>(postsResponseDto, HttpStatus.OK);
    }
}
