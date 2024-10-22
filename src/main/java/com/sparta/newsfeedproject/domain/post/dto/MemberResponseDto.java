package com.sparta.newsfeedproject.domain.post.dto;

import com.sparta.newsfeedproject.domain.member.entity.Member;
import lombok.Getter;

/**
 * MemberResponseDto를 사용하면 Member 객체의 필요한 정보만을 포함하여 응답할 수 있습니다. 이를 통해 불필요한 데이터(예: friendsList, blockedList)의 lazy-loading 문제를 피할 수 있습니다.
 */
@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String name;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
    }
}
