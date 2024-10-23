package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.config.PasswordEncoder;
import com.sparta.newsfeedproject.domain.member.dto.DeleteRequestDto;
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
    public void deleteMember(Long memberId, DeleteRequestDto requestDto,Member member) {

        Member deletedMember = memberRepository.findById(memberId).orElseThrow(()
                -> new NoSuchElementException("등록된 사용자가 없습니다."));

        if(!memberId.equals(member.getId())) throw new SecurityException("삭제할 권한이 없습니다.");

        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        if (!email.equals(deletedMember.getEmail())) throw new IllegalArgumentException("입력값이 일치하지 않습니다.");
        if (!passwordEncoder.matches(password, deletedMember.getPassword())) throw new IllegalArgumentException("입력값이 일치하지 않습니다.");

        // 사용자 삭제
        memberRepository.delete(deletedMember);
    }
}



