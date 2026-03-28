package com.hansung.logrove.domain.post;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tag_name")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TagName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 설계도의 '태그 이름 키'

    @Column(unique = true, nullable = false, length = 50)
    private String name; // 실제 태그 텍스트 (예: "노을", "서울")
}