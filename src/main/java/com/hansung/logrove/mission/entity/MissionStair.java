package com.hansung.logrove.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MISSIONS_STAIR")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MissionStair {

    @Id
    @Column(name = "mission_id") // 1. 식별 관계: Mission의 PK를 그대로 사용
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false) // 2. 객관식 또는 주관식 퀴즈
    private StairType type;

    @Column(name = "step", nullable = false) // 3. 진행 순서 (1단계, 2단계...)
    private Integer step;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false) // 4. 난이도 (EASY, NORMAL, HARD)
    private MissionLevel level;

    // 비즈니스 로직: 퀴즈 설정 변경 시 사용
    public void updateStair(StairType type, Integer step, MissionLevel level) {
        this.type = type;
        this.step = step;
        this.level = level;
    }
}