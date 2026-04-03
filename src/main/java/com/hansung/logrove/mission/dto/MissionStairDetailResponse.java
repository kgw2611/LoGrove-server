package com.hansung.logrove.mission.dto;

import com.hansung.logrove.mission.entity.StairType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionStairDetailResponse {
    private Long missionId;      // 미션 번호
    private String question;     // 미션 제목 (질문)
    private StairType type;      // [핵심] MULTIPLE_CHOICE(객관식) 또는 SHORT_ANSWER(주관식)

    // 객관식용 보기 (주관식일 땐 null로 나감)
    private String radio1;
    private String radio2;
    private String radio3;
    private String radio4;

    private String commentary;   // 해설
    private String imageUrl;     // 문제 이미지
}