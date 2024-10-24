package com.sparta.newsfeedproject.filter;

import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
public class AuthFilter implements Filter {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public AuthFilter(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(url)
                && (url.startsWith("/api/members/login")
                || url.startsWith("/api/members/signup")
                || url.startsWith("/api/post//{postId}/comments/all")
            )) {
                log.info("인증 처리를 하지 않은 URL :{}", url);
                // 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
                chain.doFilter(request, response); // 다음 Filter 로 이동
                return;
        }
        // 나머지 API 요청은 인증 처리 진행 토큰 확인
        String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

        if (!StringUtils.hasText(tokenValue)) {
            throw new IllegalArgumentException("Not Found Token");
        }

        // 토큰이 존재하면 검증 시작
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByEmail(info.getSubject())
                .orElseThrow(() -> new NullPointerException("Not Found User"));

        request.setAttribute("member", member);
        chain.doFilter(request, response); // 다음 Filter 로 이동
    }
}
