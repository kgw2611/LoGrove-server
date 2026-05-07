package com.hansung.logrove.user.dto;

import com.hansung.logrove.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private static final int[] LEVEL_THRESHOLDS = {0, 500, 1500, 3000, 5500, 9000, 13300};

    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String loginId;
    private String role;
    private Integer exp;
    private Integer level;
    private Integer progress;
    private String bio;
    private String profileUrl;

    public static UserResponse from(User user) {
        int lv = user.getLevel();
        int exp = user.getExp();
        int prevThreshold = LEVEL_THRESHOLDS[Math.min(lv - 1, LEVEL_THRESHOLDS.length - 1)];

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .loginId(user.getLoginId())
                .role(user.getRole() != null ? user.getRole().getRole() : null)
                .exp(exp)
                .level(lv)
                .progress(exp - prevThreshold)
                .bio(user.getBio())
                .profileUrl(user.getProfileUrl())
                .build();
    }
}