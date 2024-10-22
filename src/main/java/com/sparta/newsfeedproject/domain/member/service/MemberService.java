package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.config.PasswordEncoder;
import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.dto.LoginRequestDto;
import com.sparta.newsfeedproject.domain.member.dto.SignupRequestDto;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.entity.UserRoleEnum;
import com.sparta.newsfeedproject.domain.member.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC"; //ADMIN_TOKEN

    // 회원탈퇴
    public void deleteMember(Long memberId, HttpServletRequest request) {
        // 요청 헤더에서 Jwt 토큰 추출
        String token = jwtUtil.resolveToken(request);
    public void signup(SignupRequestDto requestDto) {
        String name = requestDto.getName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 토큰 유효성 겁사 및 사용자 정보 추출
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.getUserInfoFromToken(token);  // JWT에서 사용자 정보 추출
            String tokenUsername = claims.getSubject();  // 토큰에서 가져온 사용자 이름
//        Optional<Member> checkName = memberRepository.findByName(name);
//        if (checkName.isPresent()) {
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
//        }

            // 사용자 존재 여부 확인
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        String email = requestDto.getEmail();
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

            // 토큰의 사용자 정보와 요청한 memberId의 사용자 정보가 일치하는지 확인
            if (!member.getName().equals(tokenUsername)) {
                throw new IllegalArgumentException("본인만 탈퇴할 수 있습니다.");
        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())){
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            // 사용자 삭제
            memberRepository.delete(member);
        } else {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            role = UserRoleEnum.ADMIN;
        }

        Member member = new Member(name, email, password,role);
        memberRepository.save(member);
    }
}

    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("등록된 사용자가 없습니다."));

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(member.getEmail(), member.getRole());
        jwtUtil.addJwtToCookie(token,response);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    // 사용자 조회
    public Optional<Member> getMemberWithPosts(Long id) {
        return memberRepository.findById(id);
    }
}
