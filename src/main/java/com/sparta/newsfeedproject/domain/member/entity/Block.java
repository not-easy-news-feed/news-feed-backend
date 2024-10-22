package com.sparta.newsfeedproject.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_member_id")
    private Member blockerMember;

    @ManyToOne
    @JoinColumn(name = "blocked_member_id")
    private Member blockedMember;

    public Block(Member blockerMember, Member blockedMember) {
        this.blockerMember = blockerMember;
        this.blockedMember = blockedMember;
    }
}
