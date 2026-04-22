package com.hansung.logrove.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    // 실제 파일은 S3 또는 로컬에 저장, DB에는 경로(URL)만 보관
    @Column(name = "image_url", nullable = false, length = 500)
    private String url;

    // 게시글 내 이미지 정렬 순서
    @Column(name = "image_order", nullable = false)
    private int imageOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostImage(String url, int imageOrder, Post post) {
        this.url = url;
        this.imageOrder = imageOrder;
        this.post = post;
    }
}