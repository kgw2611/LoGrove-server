package com.hansung.logrove.post.service;

import com.hansung.logrove.global.exception.ErrorCode;
import com.hansung.logrove.global.exception.LoGroveException;
import com.hansung.logrove.post.dto.PostCreateRequest;
import com.hansung.logrove.post.dto.PostListResponse;
import com.hansung.logrove.post.dto.PostResponse;
import com.hansung.logrove.post.dto.PostUpdateRequest;
import com.hansung.logrove.post.entity.BoardType;
import com.hansung.logrove.post.entity.Post;
import com.hansung.logrove.post.entity.PostLike;
import com.hansung.logrove.post.entity.PostTag;
import com.hansung.logrove.post.repository.PostImageRepository;
import com.hansung.logrove.post.repository.PostLikeRepository;
import com.hansung.logrove.post.repository.PostRepository;
import com.hansung.logrove.post.repository.PostTagRepository;
import com.hansung.logrove.post.entity.*;
import com.hansung.logrove.post.repository.*;
import com.hansung.logrove.tag.entity.Tag;
import com.hansung.logrove.tag.repository.TagRepository;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.repository.UserRepository;
import com.hansung.logrove.storage.service.ImageStorageService;
import com.hansung.logrove.storage.dto.ImageUploadResult;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;

    // ── 게시글 작성 ──────────────────────────────────────────

    @Transactional
    public PostResponse createPost(Long userId, PostCreateRequest request, List<MultipartFile> images) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));

        if (request.getBoardType() == BoardType.GALLERY &&
                (images == null || images.isEmpty())) {
            throw new LoGroveException(ErrorCode.IMAGE_REQUIRED);
        }

        Post post = new Post(request.getTitle(), request.getContent(), request.getBoardType(), user);
        postRepository.save(post);

        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                ImageUploadResult result = imageStorageService.storePostImage(post.getId(), images.get(i));
                post.getImages().add(new PostImage(result.getUrl(), i, post));
            }
        }

        if (request.getTagIds() != null) {
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new LoGroveException(ErrorCode.TAG_NOT_FOUND));
                post.getTags().add(new PostTag(post, tag));
            }
        }

        return PostResponse.from(post);
    }

    // ── 게시글 상세 조회 ──────────────────────────────────────

    @Transactional
    public PostResponse getPost(Long postId) {
        Post post = findPost(postId);
        post.incrementView();
        return PostResponse.from(post);
    }

    // ── 게시판별 목록 조회 ────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<PostListResponse> getPostsByBoard(BoardType boardType, Pageable pageable) {
        return postRepository.findByBoardType(boardType, pageable)
                .map(PostListResponse::from);
    }

    // ── 게시글 수정 ──────────────────────────────────────────

    @Transactional
    public PostResponse updatePost(Long userId, Long postId, PostUpdateRequest request) {
        Post post = findPost(postId);
        validateOwner(post, userId);
        post.update(request.getTitle(), request.getContent());
        return PostResponse.from(post);
    }

    // ── 게시글 삭제 ──────────────────────────────────────────

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = findPost(postId);
        validateOwner(post, userId);
        postRepository.delete(post);
    }

    // ── 좋아요 / 취소 ────────────────────────────────────────

    @Transactional
    public void likePost(Long userId, Long postId) {
        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new LoGroveException(ErrorCode.ALREADY_LIKED);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        Post post = findPost(postId);
        postLikeRepository.save(new PostLike(user, post));
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        PostLike like = postLikeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.LIKE_NOT_FOUND));
        postLikeRepository.delete(like);
    }

    // ── 공통 헬퍼 ────────────────────────────────────────────

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateOwner(Post post, Long userId) {
        if (!post.getUser().getId().equals(userId)) {
            throw new LoGroveException(ErrorCode.FORBIDDEN);
        }
    }

    // ── 게시판 내 검색 ────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<PostListResponse> searchPosts(BoardType boardType, String title, List<Long> tagIds, Pageable pageable) {
        if (title != null && tagIds != null) {
            return postRepository.findByBoardTypeAndTitleContainingAndTagIds(boardType, title, tagIds, pageable)
                    .map(PostListResponse::from);
        }
        if (title != null) {
            return postRepository.findByBoardTypeAndTitleContaining(boardType, title, pageable)
                    .map(PostListResponse::from);
        }
        if (tagIds != null) {
            return postRepository.findByBoardTypeAndTagIds(boardType, tagIds, pageable)
                    .map(PostListResponse::from);
        }
        return postRepository.findByBoardType(boardType, pageable)
                .map(PostListResponse::from);
    }
}