package com.hansung.logrove.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 역직렬화를 위한 기본 생성자
public class CommentCreateRequest {

    // 댓글 내용 — 빈 문자열 또는 공백만 있는 경우도 유효하지 않음
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private Long parentId;
}