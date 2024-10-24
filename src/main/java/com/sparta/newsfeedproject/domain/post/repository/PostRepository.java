package com.sparta.newsfeedproject.domain.post.repository;

import com.sparta.newsfeedproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByMemberIdIn(List<Long> memberIds, Pageable pageable);
}
