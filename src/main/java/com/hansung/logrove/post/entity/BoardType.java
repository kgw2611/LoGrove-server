package com.hansung.logrove.post.entity;

/**
 * 게시판 종류 - 3가지로 고정되어 있어 Enum으로 관리.
 * → 값이 변하지 않는 고정 집합은 Enum이 DB 테이블보다 타입 안전성이 높고 간결함.
 *
 * Post 엔티티에서 @Enumerated(EnumType.STRING)으로 DB에 "COMMUNITY" 등 문자열로 저장.
 */
public enum BoardType {
    COMMUNITY,  // 커뮤니티
    GALLERY,    // 갤러리
    FORUM       // 포럼
}