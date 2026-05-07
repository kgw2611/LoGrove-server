package com.hansung.logrove.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    private String nickname;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String bio;
}