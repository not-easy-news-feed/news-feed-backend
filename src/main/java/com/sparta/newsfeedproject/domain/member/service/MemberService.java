package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.config.PasswordEncoder;
import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.dto.LoginRequestDto;
import com.sparta.newsfeedproject.domain.member.dto.MemberResponseDto;
import com.sparta.newsfeedproject.domain.member.dto.SignupRequestDto;
import com.sparta.newsfeedproject.domain.member.dto.UpdateRequestDto;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.entity.UserRoleEnum;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC"; //ADMIN_TOKEN


    public void signup(SignupRequestDto requestDto) {
        String name = requestDto.getName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        String email = requestDto.getEmail();
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        Member member = new Member(name, email, password, role);
        memberRepository.save(member);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(member.getEmail(), member.getRole());
        jwtUtil.addJwtToCookie(token, response);
    }

    @Transactional
    public MemberResponseDto updateMember(Long memberId, UpdateRequestDto requestDto, Member member) {

        Member updatedMember = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("등록된 사용자가 없습니다."));

        if(!memberId.equals(member.getId())) throw new SecurityException("수정할 권한이 없습니다.");

        String password = passwordEncoder.encode(requestDto.getPassword());
        updatedMember.update(requestDto.getName(), password);
        memberRepository.saveAndFlush(updatedMember);
        return new MemberResponseDto(updatedMember);
    }


    public Optional<Member> getMemberWithPosts(Long id) {
        return memberRepository.findById(id);
    }


    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }
}
