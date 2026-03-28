package com.hansung.logrove.domain.post;

import com.hansung.logrove.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 설계도의 '게시글 키'

    @Column(nullable = false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    private BoardType boardType; // 게시판 종류

    @Builder.Default
    private Integer viewCount = 0; // 조회수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 설계도의 '유저 키' (FK)
    private User user;

    // 하나의 게시글은 여러 이미지를 가짐
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt; // 작성 시간

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
}