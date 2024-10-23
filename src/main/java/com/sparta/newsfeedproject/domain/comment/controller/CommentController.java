package com.sparta.newsfeedproject.domain.comment.controller;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.comment.service.CommentService;
import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtUtil jwtUtil;
    private final MemberService  memberService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request //Spring Security+JWT 인증 이후에 @AuthenticationPrincipal UserDetailsImpl 로 파라미터를 받는부분 필요
            ) {
        String token = jwtUtil.getTokenFromRequest(request);//토큰 가져오기
        String tokenValue = jwtUtil.substringToken(token);//자르기
        //사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);
        String email = claims.getSubject();
        // Member 객체 조회
        Member member = memberService.findByEmail(email);
        if(member == null) {
            log.info("유효하지 않은 사용자입니다.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CommentResponseDto responseDto = commentService.createComment(postId, requestDto, member);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        String token = jwtUtil.getTokenFromRequest(request);//토큰 가져오기
        String tokenValue = jwtUtil.substringToken(token);//자르기
        //사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);
        String email = claims.getSubject();
        // Member 객체 조회
        Member member = memberService.findByEmail(email);
        if(member == null) {
            log.info("유효하지 않은 commet사용자 입니다.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CommentResponseDto responseDto = commentService.updateComment(postId,commentId,requestDto,member);
        return ResponseEntity.ok(responseDto);
    }
}
