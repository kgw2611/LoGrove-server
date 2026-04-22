package com.hansung.logrove.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stair_mission") // DB 테이블명과 매핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MissionStairContent {

    @Id
    @Column(name = "mission_id") // MissionStair의 PK를 그대로 상속받는 식별자
    private Long id;

    @MapsId // 1. 식별 관계 설정: 부모(MissionStair)의 ID를 자신의 PK로 사용
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private MissionStair missionStair;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer; // [공통] 정답 (객관식은 선지 번호, 주관식은 텍스트)

    // [객관식 전용] 선지 1~4번 (주관식일 경우 DB에는 null로 저장됨)
    @Column(name = "radio1", columnDefinition = "TEXT")
    private String radio1;

    @Column(name = "radio2", columnDefinition = "TEXT")
    private String radio2;

    @Column(name = "radio3", columnDefinition = "TEXT")
    private String radio3;

    @Column(name = "radio4", columnDefinition = "TEXT")
    private String radio4;

    @Column(name = "commentary", columnDefinition = "TEXT")
    private String commentary; // [공통] 문제 풀이 해설

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl; // [공통] 문제에 첨부될 이미지 경로

    /**
     * 비즈니스 로직: 문제 유형 확인 메서드
     * 서비스 계층에서 유형별로 로직을 태울 때 편리하게 사용합니다.
     */
    public boolean isMultipleChoice() {
        return this.missionStair.getType() == StairType.MULTIPLE_CHOICE;
    }

    public boolean isShortAnswer() {
        return this.missionStair.getType() == StairType.SHORT_ANSWER;
    }

    // 데이터 수정 메서드 (관리자 기능 등에서 활용)
    public void updateContent(String answer, String r1, String r2, String r3, String r4, String commentary) {
        this.answer = answer;
        this.radio1 = r1;
        this.radio2 = r2;
        this.radio3 = r3;
        this.radio4 = r4;
        this.commentary = commentary;
    }
}