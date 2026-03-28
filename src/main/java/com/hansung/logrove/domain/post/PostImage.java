package com.hansung.logrove.domain.post;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이미지 키

    @Column(nullable = false, length = 500)
    private String url; // 사진 경로

    @Column(name = "image_order")
    private Integer order; // 사진 순서 (설계도의 order)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 설계도의 '게시글 키' (FK)
    private Post post;
}