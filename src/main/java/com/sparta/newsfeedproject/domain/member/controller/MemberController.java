package com.sparta.newsfeedproject.domain.member.controller;

import com.sparta.newsfeedproject.domain.member.dto.DeleteRequestDto;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId,
                                               @RequestBody DeleteRequestDto requestDto,
                                               HttpServletRequest request
    ) {
        Member member = (Member) request.getAttribute("member");
        memberService.deleteMember(memberId, requestDto, member);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("회원탈퇴 완료");
    }
}


