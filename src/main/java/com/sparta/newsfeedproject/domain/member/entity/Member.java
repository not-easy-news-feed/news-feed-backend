package com.sparta.newsfeedproject.domain.member.entity;

import com.sparta.newsfeedproject.domain.common.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // 친구
    @ManyToMany
    @JoinTable(
            name="friends",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<Member> friendsList = new HashSet<Member>();

    // 차단친구
    @ManyToMany
    @JoinTable(
            name="blocked_friends",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_friend_id")
    )
    private Set<Member> blockedList = new HashSet<>();

}
