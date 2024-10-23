package com.sparta.newsfeedproject.domain.member.repository;

import com.sparta.newsfeedproject.domain.member.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByBlockerMemberIdAndBlockedMemberId(Long blockerMemberId, Long blockedMemberId);
}
