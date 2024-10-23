package com.sparta.newsfeedproject.domain.member.controller;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.dto.LoginRequestDto;
import com.sparta.newsfeedproject.domain.member.dto.MemberResposeDto;
import com.sparta.newsfeedproject.domain.member.dto.SignupRequestDto;
import com.sparta.newsfeedproject.domain.member.dto.UpdateRequestDto;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private JwtUtil jwtUtil;


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

    // 프로필 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMember(@PathVariable Long memberId) {
        Optional<Member> member = memberService.getMemberWithPosts(memberId);

        // 프로필이 존재하지 않는 경우 404
        if (member.isEmpty()) {
            return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(member.get(), HttpStatus.OK);
    }

    // 프로필 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId, @RequestBody UpdateRequestDto requestDto, HttpServletRequest request) {

        //토큰 가져오기
        String token = jwtUtil.getTokenFromRequest(request);
        //자르기
        String tokenValue = jwtUtil.substringToken(token);
        //사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);
        String email = claims.getSubject();
        // Member 객체 조회
        Member member = memberService.findByEmail(email);
        if (member == null) {
            log.info("유효하지 않은 사용자 입니다.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        Member responseDto = memberService.updateMember(memberId, requestDto, token);

        return ResponseEntity.ok(responseDto);



    }
}


