package com.sparta.newsfeedproject.domain.member.repository;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRepository  extends JpaRepository<Member, Long> {
}
