package com.hansung.logrove.comment.entity;

import com.hansung.logrove.post.entity.Post;
import com.hansung.logrove.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMMENTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자, 외부 직접 생성 방지
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT") // 댓글 본문 (길이 제한 없이 TEXT 타입)
    private String content;

    @CreationTimestamp // INSERT 시점에 자동으로 현재 시각 주입
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 댓글은 반드시 하나의 게시글에 속함 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 댓글은 반드시 한 명의 작성자에 속함 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 댓글 좋아요 목록 (1:N) — 댓글 삭제 시 좋아요도 함께 삭제 (orphanRemoval)
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likes = new ArrayList<>();

    @Builder
    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

    // 댓글 수정 — 변경 감지(Dirty Checking)를 활용하기 위해 setter 대신 메서드로 제공
    public void update(String content) {
        this.content = content;
    }
}