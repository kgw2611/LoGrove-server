package com.hansung.logrove.domain.comment;

import com.hansung.logrove.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 설계도의 '댓글 좋아요 키'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id") // 설계도의 '댓글 키' (FK)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 설계도의 '유저 키' (FK)
    private User user;
}