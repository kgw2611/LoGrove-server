package com.hansung.logrove.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),

    // 게시글
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 내역을 찾을 수 없습니다."),
    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "갤러리 게시판은 이미지가 필수입니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // 태그
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다."),

    // 미션
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "미션을 찾을 수 없습니다."),
    MISSION_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 완료된 미션입니다.");

    private final HttpStatus status;
    private final String message;
}