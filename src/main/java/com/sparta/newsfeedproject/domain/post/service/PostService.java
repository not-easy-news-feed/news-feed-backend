package com.sparta.newsfeedproject.domain.post.service;

import com.sparta.newsfeedproject.domain.member.entity.Follow;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.member.repository.MemberRepository;
import com.sparta.newsfeedproject.domain.post.dto.PostRequestDto;
import com.sparta.newsfeedproject.domain.post.dto.PostResponseDto;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, Member member) {
        Post post = new Post(requestDto, member);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        // 작성자 검증
        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다. 게시물을 수정할 권한이 없습니다.");
        }

        post.updateData(requestDto);
        postRepository.saveAndFlush(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        if (!post.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("작성자가 아닙니다. 게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.deleteById(postId);
    }

    public Page<PostResponseDto> getPosts(Pageable pageable) {

        return postRepository.findAll(pageable).map(PostResponseDto::new);
    }

    @Transactional
    public Page<PostResponseDto> getFriendPosts(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new NoSuchElementException("no"));
        List<Long> friendIdList = member.getFollowingList()
                .stream()
                .map(Follow::getFollowedMember)
                .filter(followedMember -> followedMember.getFollowingList()
                        .stream()
                        .map(Follow::getFollowedMember)
                        .toList()
                        .contains(member))
                .map(Member::getId)
                .toList();
        return postRepository.findAllByMemberIdIn(friendIdList, pageable).map(PostResponseDto::new);
    }
}
