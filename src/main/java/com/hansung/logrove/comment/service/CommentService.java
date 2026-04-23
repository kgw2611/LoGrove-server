package com.hansung.logrove.comment.service;

import com.hansung.logrove.comment.dto.CommentCreateRequest;
import com.hansung.logrove.comment.dto.CommentResponse;
import com.hansung.logrove.comment.entity.Comment;
import com.hansung.logrove.comment.entity.CommentLike;
import com.hansung.logrove.comment.repository.CommentLikeRepository;
import com.hansung.logrove.comment.repository.CommentRepository;
import com.hansung.logrove.global.exception.ErrorCode;
import com.hansung.logrove.global.exception.LoGroveException;
import com.hansung.logrove.post.entity.Post;
import com.hansung.logrove.post.repository.PostRepository;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // ── 댓글 목록 조회 ──────────────────────────────────────────
    // 특정 게시글의 전체 댓글을 작성 시간 오름차순으로 반환
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId, Long userId) {
        // 게시글 존재 여부 먼저 확인
        validatePost(postId);
        return commentRepository.findByPost_IdOrderByCreatedAtAsc(postId)
                .stream()
                .map(comment -> {
                    boolean isLiked = userId != null &&
                            commentLikeRepository.existsByUser_IdAndComment_Id(userId, comment.getId());
                    return CommentResponse.from(comment, isLiked);
                })
                .collect(Collectors.toList());
    }

    // ── 댓글 단건 조회 ──────────────────────────────────────────
    @Transactional(readOnly = true)
    public CommentResponse getComment(Long postId, Long commentId, Long userId) {
        validatePost(postId);
        Comment comment = findComment(commentId);
        boolean isLiked = userId != null &&
                commentLikeRepository.existsByUser_IdAndComment_Id(userId, comment.getId());
        return CommentResponse.from(comment, isLiked);
    }

    // ── 댓글 작성 ───────────────────────────────────────────────
    @Transactional
    public CommentResponse createComment(Long postId, Long userId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();

        return CommentResponse.from(commentRepository.save(comment), false);
    }

    // ── 댓글 수정 ───────────────────────────────────────────────
    // 작성자 본인만 수정 가능 — userId 비교로 권한 체크
    @Transactional
    public CommentResponse updateComment(Long postId, Long commentId, Long userId, CommentCreateRequest request) {
        validatePost(postId);
        Comment comment = findComment(commentId);
        validateOwner(comment, userId); // 본인 댓글 여부 확인

        // 변경 감지(Dirty Checking)로 별도 save() 호출 없이 UPDATE 쿼리 실행
        comment.update(request.getContent());
        boolean isLiked = commentLikeRepository.existsByUser_IdAndComment_Id(userId, commentId);
        return CommentResponse.from(comment, isLiked);
    }

    // ── 댓글 삭제 ───────────────────────────────────────────────
    // 작성자 본인만 삭제 가능
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        validatePost(postId);
        Comment comment = findComment(commentId);
        validateOwner(comment, userId);

        commentRepository.delete(comment);
    }

    // ── 댓글 좋아요 ─────────────────────────────────────────────
    @Transactional
    public void likeComment(Long commentId, Long userId) {
        // 이미 좋아요를 눌렀는지 확인 → 중복 방지
        if (commentLikeRepository.existsByUser_IdAndComment_Id(userId, commentId)) {
            throw new LoGroveException(ErrorCode.ALREADY_LIKED);
        }

        Comment comment = findComment(commentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));

        commentLikeRepository.save(CommentLike.builder()
                .comment(comment)
                .user(user)
                .build());
    }

    // ── 댓글 좋아요 취소 ────────────────────────────────────────
    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        CommentLike like = commentLikeRepository
                .findByUser_IdAndComment_Id(userId, commentId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.LIKE_NOT_FOUND));

        commentLikeRepository.delete(like);
    }

    // ── 내부 헬퍼 메서드 ────────────────────────────────────────

    // 게시글 존재 여부 검증 (댓글 조회 전 선행 체크)
    private void validatePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new LoGroveException(ErrorCode.POST_NOT_FOUND);
        }
    }

    // 댓글 조회 — 없으면 예외 발생
    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.COMMENT_NOT_FOUND));
    }

    // 댓글 작성자 본인 여부 확인 — 다른 유저가 수정/삭제 시도 시 예외 발생
    private void validateOwner(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new LoGroveException(ErrorCode.FORBIDDEN);
        }
    }

    // ── 내 댓글 목록 조회 ────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<CommentResponse> getMyComments(Long userId) {
        return commentRepository.findByUser_Id(userId).stream()
                .map(comment -> CommentResponse.from(comment, true))
                .collect(Collectors.toList());
    }
}