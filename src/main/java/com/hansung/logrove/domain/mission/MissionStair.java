package com.hansung.logrove.domain.mission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mission_stair")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStair {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private MissionType type; // ENUM: 타입

    private Integer step; // 문제 순서

    @Enumerated(EnumType.STRING)
    private LevelType level; // ENUM: 난이도
}