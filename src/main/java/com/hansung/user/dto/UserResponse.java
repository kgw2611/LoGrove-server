package com.hansung.user.dto;

import com.hansung.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String loginId;
    private String role;
    private Integer exp;
    private Integer level;
    private Integer progress;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .loginId(user.getLoginId())
                .role(user.getRole() != null ? user.getRole().getRole() : null)
                .exp(user.getExp())
                .level(user.getLevel())
                .progress(user.getProgress())
                .build();
    }
}