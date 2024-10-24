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
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");
        CommentResponseDto responseDto = commentService.createComment(postId, requestDto, member);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");
        CommentResponseDto responseDto = commentService.updateComment(postId, commentId, requestDto, member);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");
        commentService.deleteComment(postId, commentId, member);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("댓글이 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/all")
    public ResponseEntity<PostCommentsResponseDto> getComments(@PathVariable Long postId) {
        //게시글의 댓글은 로그인 없어도 볼 수 있다.
        PostCommentsResponseDto responseDto = commentService.getComments(postId);
        return ResponseEntity.ok(responseDto);
    }
}
