package com.hansung.logrove.domain.post;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 설계도의 '태그 키'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 설계도의 '게시물 키' (FK)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_name_id") // 설계도의 '태그 이름 키' (FK)
    private TagName tagName;
}