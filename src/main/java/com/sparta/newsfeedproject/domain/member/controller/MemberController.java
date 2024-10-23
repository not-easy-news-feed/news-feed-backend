package com.sparta.newsfeedproject.domain.member.controller;

import com.sparta.newsfeedproject.domain.member.dto.*;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        memberService.login(requestDto, response);
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowResponseDto> createFollow(HttpServletRequest servletRequest, @RequestBody FollowRequestDto requestDto) {
        Member member = (Member) servletRequest.getAttribute("member");
        FollowResponseDto responseDto = memberService.createFollow(member, requestDto.getFollowedMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/follow")
    public ResponseEntity<String> deleteFollow(HttpServletRequest servletRequest, @RequestBody FollowRequestDto requestDto) {
        Member member = (Member) servletRequest.getAttribute("member");
        memberService.deleteFollow(member, requestDto.getFollowedMemberId());
        return ResponseEntity.ok("팔로우 해제 성공");
    }

    @PostMapping("/block")
    public ResponseEntity<BlockResponseDto> createBlock(HttpServletRequest request, @RequestBody BlockRequestDto requestDto) {
        Member member = (Member) request.getAttribute("member");
        BlockResponseDto responseDto = memberService.createBlock(member, requestDto.getBlockedMemberId());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/block")
    public ResponseEntity<String> deleteBlock(HttpServletRequest request, @RequestBody BlockRequestDto requestDto) {
        Member member = (Member) request.getAttribute("member");
        memberService.deleteBlock(member, requestDto.getBlockedMemberId());
        return ResponseEntity.ok("차단 해제 성공");
    }
}
