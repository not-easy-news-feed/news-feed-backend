package com.sparta.newsfeedproject.domain.member.entity;

import com.sparta.newsfeedproject.domain.common.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Member extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role = UserRoleEnum.USER;//기본값을 일반사용자로

    // 내가 팔로우 하는 팔로우 리스트
    @OneToMany(mappedBy = "followerMember")
    private List<Follow> followingList = new ArrayList<>();

    // 차단친구
    @ManyToMany
    @JoinTable(
            name="blocked_friends",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_friend_id")
    )
    private Set<Member> blockedList = new HashSet<>();

    public Member(String name, String email, String password, UserRoleEnum role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
