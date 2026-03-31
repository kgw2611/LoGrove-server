package com.hansung.logrove.tag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 태그명 테이블 (TAGNAME)
 * Vision API가 동적으로 생성하는 태그를 저장하므로 ENUM이 아닌 VARCHAR로 관리.
 * (ex. "portrait", "landscape", "sunset" 등 API 결과에 따라 자유롭게 추가됨)
 */
@Entity
@Table(name = "TAGNAME")
@Getter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tac_id")
    private Long id;

    @Column(name = "tac", nullable = false, length = 50, unique = true)
    private String name;

    public Tag(String name) {
        this.name = name;
    }
}