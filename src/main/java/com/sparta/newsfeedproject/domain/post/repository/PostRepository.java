package com.sparta.newsfeedproject.domain.post.repository;

import com.sparta.newsfeedproject.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    default Post findByPostId(Long id){
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
    }
}
