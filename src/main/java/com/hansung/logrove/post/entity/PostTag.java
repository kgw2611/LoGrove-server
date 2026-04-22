package com.hansung.logrove.post.entity;

import com.hansung.logrove.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 ↔ 태그 중간 테이블 (TAGS)
 * Post와 Tag의 N:M 관계를 1:N + N:1로 풀어낸 연결 엔티티.
 * Vision API가 분석한 태그 또는 사용자가 직접 등록한 태그 모두 이 테이블에 저장됨.
 */
@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 실제 태그명은 TAGNAME 테이블에서 관리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}