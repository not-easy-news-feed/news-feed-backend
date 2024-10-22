package com.sparta.newsfeedproject.domain.member.controller;

import com.sparta.newsfeedproject.domain.member.entity.Follow;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.FollowRepository;
import com.sparta.newsfeedproject.domain.member.dto.LoginRequestDto;
import com.sparta.newsfeedproject.domain.member.dto.SignupRequestDto;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sparta.newsfeedproject.domain.member.entity.Follow;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.FollowRepository;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto){
        memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        memberService.login(requestDto,response);
        return ResponseEntity.ok("로그인 성공");
    }

    private MemberRepository memberRepository;
    private FollowRepository followRepository;

    @PostMapping("/follow")
    public void followMember(@RequestBody Long memberId, @RequestBody Long followedId) {
        Member follower = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 id: " + memberId));
        Member followed = memberRepository.findById(followedId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 id: " + followedId));
        Optional<Follow> followCheck = followRepository.findByFollowerIdAndFollowedId(follower.getId(), followedId);
        if(followCheck.isPresent()) throw new RuntimeException("이미 팔로우 중인 유저입니다.");

        Follow follow = new Follow(follower, followed);
        followRepository.save(follow);
    }
}
