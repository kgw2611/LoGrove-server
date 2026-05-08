package com.hansung.logrove.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions_stair")
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
    @Column(name = "type", nullable = false)
    private StairType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private MissionLevel level;

    public void updateStair(StairType type, MissionLevel level) {
        this.type = type;
        this.level = level;
    }
}