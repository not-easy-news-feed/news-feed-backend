package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.config.PasswordEncoder;
import com.sparta.newsfeedproject.domain.member.dto.MemberResponseDto;
import com.sparta.newsfeedproject.domain.member.dto.UpdateRequestDto;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDto updateMember(Long memberId, UpdateRequestDto requestDto, Member member) {

        Member updatedMember = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("등록된 사용자가 없습니다."));

        if(!memberId.equals(member.getId())) throw new SecurityException("수정할 권한이 없습니다.");

        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");

        if (requestDto.getUpdatedPassword().equals(requestDto.getPassword())) throw new IllegalArgumentException("현재 비밀번호와 동일합니다.");

        String password = passwordEncoder.encode(requestDto.getUpdatedPassword());

        updatedMember.update(requestDto.getUpdatedName(), password);


        memberRepository.saveAndFlush(updatedMember);
        return new MemberResponseDto(updatedMember);
    }



    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }
}
