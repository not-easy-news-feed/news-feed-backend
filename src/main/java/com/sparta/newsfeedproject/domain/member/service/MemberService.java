package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원탈퇴
    public void deleteMember(Long memberId) {
        // 사용자 존재 여부 확인
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자 삭제
        memberRepository.delete(member);
    }
}



