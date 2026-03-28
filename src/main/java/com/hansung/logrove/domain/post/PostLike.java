package com.hansung.logrove.domain.post;

import com.hansung.logrove.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_like") // DB의 'post_like' 테이블과 매핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 설계도상의 PK (유저키+게시글키 조합 대신 단독 PK 사용이 관례)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 설계도의 '게시글 키' (FK)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 설계도의 '유저 키' (FK)
    private User user;
}