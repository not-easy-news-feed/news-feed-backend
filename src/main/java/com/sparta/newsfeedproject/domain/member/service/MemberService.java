package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    // 회원탈퇴
    public void deleteMember(Long memberId, HttpServletRequest request) {
        // 요청 헤더에서 Jwt 토큰 추출
        String token = jwtUtil.resolveToken(request);

        // 토큰 유효성 겁사 및 사용자 정보 추출
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);  // JWT에서 사용자 정보 추출
            String tokenUsername = claims.getSubject();  // 토큰에서 가져온 사용자 이름

            // 사용자 존재 여부 확인
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 토큰의 사용자 정보와 요청한 memberId의 사용자 정보가 일치하는지 확인
            if (!member.getName().equals(tokenUsername)) {
                throw new IllegalArgumentException("본인만 탈퇴할 수 있습니다.");
            }
            // 사용자 삭제
            memberRepository.delete(member);
        } else {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}



