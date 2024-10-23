package com.sparta.newsfeedproject.domain.member.controller;

import com.sparta.newsfeedproject.domain.member.dto.MemberResponseDto;
import com.sparta.newsfeedproject.domain.member.dto.UpdateRequestDto;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    // 프로필 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId, @RequestBody UpdateRequestDto requestDto, HttpServletRequest request) {


        Member member = (Member) request.getAttribute("member");

        MemberResponseDto responseDto = memberService.updateMember(memberId, requestDto, member);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);


    }
}


