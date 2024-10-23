package com.sparta.newsfeedproject.domain.member.service;

import com.sparta.newsfeedproject.config.PasswordEncoder;
import com.sparta.newsfeedproject.domain.jwt.JwtUtil;
import com.sparta.newsfeedproject.domain.member.dto.*;
import com.sparta.newsfeedproject.domain.member.entity.Block;
import com.sparta.newsfeedproject.domain.member.entity.Follow;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.entity.UserRoleEnum;
import com.sparta.newsfeedproject.domain.member.repository.BlockRepository;
import com.sparta.newsfeedproject.domain.member.repository.FollowRepository;
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
    private final FollowRepository followRepository;
    private final BlockRepository blockRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC"; //ADMIN_TOKEN

    public void signup(SignupRequestDto requestDto) {
        String name = requestDto.getName();
        String password = passwordEncoder.encode(requestDto.getPassword());

        String email = requestDto.getEmail();
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if(checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()) {
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
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

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(member.getEmail(), member.getRole());
        jwtUtil.addJwtToCookie(token, response);
    }
    public Optional<Member> getMemberWithPosts(Long id) {
        return memberRepository.findById(id);
    }

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

    public FollowResponseDto createFollow(Member follower, Long followedMemberId) {
        Member followed = memberRepository.findById(followedMemberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        Optional<Follow> followCheck = followRepository.findByFollowerMemberIdAndFollowedMemberId(follower.getId(), followedMemberId);
        if(followCheck.isPresent())
            throw new RuntimeException("이미 팔로우 중인 유저입니다.");
        Follow follow = new Follow(follower, followed);
        return new FollowResponseDto(followRepository.save(follow));
    }

    public void deleteFollow(Member follower, Long followedMemberId) {
        memberRepository.findById(followedMemberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        Follow follow = followRepository.findByFollowerMemberIdAndFollowedMemberId(follower.getId(), followedMemberId)
                .orElseThrow(() -> new NoSuchElementException("팔로우 중인 유저가 아닙니다."));
        followRepository.delete(follow);
    }

    public BlockResponseDto createBlock(Member blockerMember, Long blockedMemberId) {
        Member blockedMember = memberRepository.findById(blockedMemberId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        Optional<Block> blockCheck = blockRepository.findByBlockerMemberIdAndBlockedMemberId(blockerMember.getId(), blockedMemberId);
        if(blockCheck.isPresent()) throw new RuntimeException("이미 차단 중인 유저입니다.");

        Block block = new Block(blockedMember, blockerMember);
        return new BlockResponseDto(blockRepository.save(block));
    }

    public void deleteBlock(Member blocker, Long blockedMemberId) {
        memberRepository.findById(blockedMemberId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        Block block = blockRepository.findByBlockerMemberIdAndBlockedMemberId(blocker.getId(), blockedMemberId)
                .orElseThrow(() -> new NoSuchElementException("차단 중인 유저가 아닙니다."));
        blockRepository.delete(block);
    }
}

