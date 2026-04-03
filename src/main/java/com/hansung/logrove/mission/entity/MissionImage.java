package com.hansung.logrove.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MISSIONS_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MissionImage {

    @Id // 1. 부모(Mission)의 PK를 그대로 이어받습니다.
    private Long id;

    @MapsId // 2. 핵심! Mission 엔티티의 ID를 내 PK로 사용하겠다는 선언입니다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id") // 3. DB 컬럼명은 mission_id가 됩니다.
    private Mission mission;

    @Column(name = "theme", nullable = false, length = 40) // 4. AI가 체크할 키워드 (예: "Sea")
    private String theme;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "guide", columnDefinition = "TEXT") // 5. 촬영 팁 등 가이드 정보
    private String guide;

    @Column(name = "sample_url", columnDefinition = "TEXT") // 6. 예시 이미지 S3 주소
    private String sampleUrl;

    @Column(name = "pass_score", nullable = false)
    @Builder.Default
    private Integer passScore = 70; // 7. 기본 합격 점수 설정

    // 비즈니스 로직: 가이드나 테마 수정 시 사용
    public void updateImageMission(String theme, String content, String guide, Integer passScore) {
        this.theme = theme;
        this.content = content;
        this.guide = guide;
        this.passScore = passScore;
    }
}