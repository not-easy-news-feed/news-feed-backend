package com.sparta.newsfeedproject.domain.comment.controller;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.comment.dto.PostCommentsResponseDto;
import com.sparta.newsfeedproject.domain.comment.service.CommentService;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
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

    //댓글 조회
    @GetMapping("/{postId}/comments/all")
    public ResponseEntity<PostCommentsResponseDto> getComments(@PathVariable Long postId) { //게시글의 댓글자체는 로그인 없어도 볼 수 있다.
        PostCommentsResponseDto responseDto = commentService.getComments(postId);
        return ResponseEntity.ok(responseDto);
    }
}
