package com.sparta.newsfeedproject.domain.comment.controller;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.comment.service.CommentService;
import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    //댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request //Spring Security+JWT 인증 이후에 @AuthenticationPrincipal UserDetailsImpl 로 파라미터를 받는부분 필요
    ) {
        // Member 객체 조회
        Member member = (Member) request.getAttribute("member");
        CommentResponseDto responseDto = commentService.createComment(postId, requestDto, member);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    //댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        // Member 객체 조회
        Member member = (Member) request.getAttribute("member");
        CommentResponseDto responseDto = commentService.updateComment(postId, commentId, requestDto, member);
        return ResponseEntity.ok(responseDto);
    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");
        commentService.deleteComment(postId, commentId, member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{postId}/comments/all")
    public ResponseEntity<PostCommentsResponseDto> getComments(@PathVariable Long postId) { //게시글의 댓글자체는 로그인 없어도 볼 수 있다.
        Post post = postService.findPostById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        PostCommentsResponseDto responseDto = commentService.getComments(post);
        return ResponseEntity.ok(responseDto);
    }
}
