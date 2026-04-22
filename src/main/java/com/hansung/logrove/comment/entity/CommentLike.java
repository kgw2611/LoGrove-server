package com.hansung.logrove.comment.entity;

import com.hansung.logrove.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "comment_like",
        // 동일 유저가 같은 댓글에 중복 좋아요를 누르는 것을 DB 레벨에서 방지
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "comment_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    // 좋아요를 누른 사용자 (N:1) — 지연 로딩으로 불필요한 쿼리 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 좋아요가 달린 댓글 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Builder
    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}