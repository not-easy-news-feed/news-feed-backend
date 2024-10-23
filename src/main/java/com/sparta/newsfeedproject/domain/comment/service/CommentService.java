package com.sparta.newsfeedproject.domain.comment.service;

import com.sparta.newsfeedproject.domain.comment.dto.CommentRequestDto;
import com.sparta.newsfeedproject.domain.comment.dto.CommentResponseDto;
import com.sparta.newsfeedproject.domain.comment.dto.PostCommentsResponseDto;
import com.sparta.newsfeedproject.domain.comment.entity.Comment;
import com.sparta.newsfeedproject.domain.comment.repository.CommentRepository;
import com.sparta.newsfeedproject.domain.member.entity.Member;
import com.sparta.newsfeedproject.domain.post.entity.Post;
import com.sparta.newsfeedproject.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, Member member) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = new Comment(requestDto.getContent(), post, member);
        commentRepository.save(comment); //생성 및 저장

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("게시글에 댓글이 존재하지 않습니다."));
        if (!comment.getMember().getId().equals(member.getId())) {
            throw new SecurityException("본인의 댓글만 수정할 수 있습니다.");
        }
        comment.update(requestDto.getContent());
        commentRepository.saveAndFlush(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if(!comment.getMember().getId().equals(member.getId())) throw new SecurityException("본인의 댓글만 삭제할 수 있습니다.");
        commentRepository.delete(comment);
    }

    public PostCommentsResponseDto getComments(Post post) {
        List<Comment> comments = commentRepository.findAllByPost(post);
        //댓글을 DTO 로 변환
        List<CommentResponseDto> commentResponseDtoList = comments.stream().map(CommentResponseDto::new).toList();
        return new PostCommentsResponseDto(post, commentResponseDtoList);
    }
}
