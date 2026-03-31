package com.hansung.logrove.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // 공통
    OK(HttpStatus.OK, "요청이 성공했습니다."),
    CREATED(HttpStatus.CREATED, "생성되었습니다."),
    DELETED(HttpStatus.OK, "삭제되었습니다."),

    // 사용자
    USER_CREATED(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    USER_UPDATED(HttpStatus.OK, "회원정보가 수정되었습니다."),
    USER_DELETED(HttpStatus.OK, "회원탈퇴가 완료되었습니다."),

    // 게시글
    POST_CREATED(HttpStatus.CREATED, "게시글이 작성되었습니다."),
    POST_UPDATED(HttpStatus.OK, "게시글이 수정되었습니다."),
    POST_DELETED(HttpStatus.OK, "게시글이 삭제되었습니다."),

    // 댓글
    COMMENT_CREATED(HttpStatus.CREATED, "댓글이 작성되었습니다."),
    COMMENT_UPDATED(HttpStatus.OK, "댓글이 수정되었습니다."),
    COMMENT_DELETED(HttpStatus.OK, "댓글이 삭제되었습니다."),
    COMMENT_LIKED(HttpStatus.OK, "댓글 좋아요가 완료되었습니다."),
    COMMENT_UNLIKED(HttpStatus.OK, "댓글 좋아요가 취소되었습니다."),

    // 미션
    MISSION_SUBMITTED(HttpStatus.OK, "미션이 제출되었습니다.");

    private final HttpStatus status;
    private final String message;
}