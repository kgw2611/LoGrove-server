package com.hansung.logrove.domain.comment;

import com.hansung.logrove.domain.post.Post;
import com.hansung.logrove.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 설계도의 '댓글 키' (PK)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 설계도의 '내용'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 설계도의 '게시물 키' (FK)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 설계도의 '유저 키' (FK)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt; // 설계도의 '작성시간'
}