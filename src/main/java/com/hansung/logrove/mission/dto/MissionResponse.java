package com.hansung.logrove.mission.dto;

import com.hansung.logrove.mission.entity.StairType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * [공통 응답 DTO]
 * 단계별 학습과 사진 제출형 학습 리스트에서 공통으로 사용하는 규격입니다.
 */
@Getter
@AllArgsConstructor
public class MissionResponse {
    private static final Map<String, String> SAMPLE_IMAGE_EXTENSIONS = Map.ofEntries(
            Map.entry("꽃", ".jpg"),
            Map.entry("윤슬", ".jpg"),
            Map.entry("인물", ".jpg")
    );

    private Long missionId;      // MISSIONS 테이블의 기본키 (식별자)
    private String title;        // 화면에 표시될 미션의 메인 제목
    private String description;  // 미션의 상세 설명이나 단계 정보
    private int level;           // 미션의 난이도 (0: 기본, 1: 초보 등)
    private String state;        // 유저별 진행 상태 (COMPLETED: 보라색, INCOMPLETE: 초록색)
    private StairType type;      // 단계별 미션 타입 (MULTIPLE_CHOICE/SHORT_ANSWER), 사진 미션은 null
    private String sampleUrl;    // 사진 미션 예시 이미지 URL, 단계별 미션은 null
    private int point;           // 미션 완료 시 지급 경험치

    public MissionResponse(Long missionId, String title, String description, int level, String state, StairType type) {
        this(missionId, title, description, level, state, type, null, 0);
    }

    public static String resolveSampleUrl(String theme, String sampleUrl) {
        if (sampleUrl != null && !sampleUrl.isBlank()) {
            return sampleUrl;
        }

        if (theme == null || theme.isBlank()) {
            return null;
        }

        return "/images/samples/" + theme + SAMPLE_IMAGE_EXTENSIONS.getOrDefault(theme, ".png");
    }
}
