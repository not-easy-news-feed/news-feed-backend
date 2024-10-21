package com.sparta.newsfeedproject.domain.member.repository;

import com.sparta.newsfeedproject.domain.member.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowerId(Long id);
    List<Follow> findAllByFollowedId(Long id);
}
