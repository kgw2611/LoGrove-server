package com.hansung.logrove.domain.mission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId; // 미션번호

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question; // 문제

    @Column(nullable = false)
    @Builder.Default
    private Integer point = 0; // 포인트
}