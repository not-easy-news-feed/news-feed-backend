package com.sparta.newsfeedproject.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_member_id")
    private Member followerMember;

    @ManyToOne
    @JoinColumn(name = "followed_member_id")
    private Member followedMember;

    public Follow(Member followerMember, Member followedMember) {
        this.followerMember = followerMember;
        this.followedMember = followedMember;
    }
}
