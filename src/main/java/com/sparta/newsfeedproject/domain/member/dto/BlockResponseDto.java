package com.sparta.newsfeedproject.domain.member.dto;

import com.sparta.newsfeedproject.domain.member.entity.Block;
import lombok.Getter;

@Getter
public class BlockResponseDto {
    private final Long id;
    private final Long blockerMemberId;
    private final Long blockedMemberId;

    public BlockResponseDto(Block block) {
        this.id = block.getId();
        this.blockerMemberId = block.getBlockerMember().getId();
        this.blockedMemberId = block.getBlockedMember().getId();
    }
}
