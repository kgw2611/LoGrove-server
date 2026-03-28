package com.hansung.logrove.domain.mission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions_image")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MissionImage {
    @Id
    private Long missionId; // Mission의 PK를 그대로 사용

    @MapsId // Mission 엔티티의 ID를 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(length = 40, nullable = false)
    private String theme; // 미션 주제

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 미션 텍스트

    @Column(columnDefinition = "TEXT")
    private String guide; // 가이드

    @Column(columnDefinition = "TEXT")
    private String sampleUrl; // 샘플 이미지

    @Column(nullable = false)
    @Builder.Default
    private Integer passScore = 70; // 점수 기준
}