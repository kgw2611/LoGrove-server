package com.hansung.logrove.user.dto;

import com.hansung.logrove.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameProfileResponse {
    private String nickname;
    private Integer level;
    private Integer exp;
    private Integer progress;

    public static GameProfileResponse from(User user) {
        return GameProfileResponse.builder()
                .nickname(user.getNickname())
                .level(user.getLevel())
                .exp(user.getExp())
                .progress(user.getProgress())
                .build();
    }
}