package com.sparta.newsfeedproject.domain.comment.repository;

import com.sparta.newsfeedproject.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
