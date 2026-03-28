package com.hansung.logrove.domain.mission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_stair")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageStair {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stair_id")
    private MissionStair missionStair;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer; // 미션 정답

    @Column(columnDefinition = "TEXT")
    private String radio1; // 선지1
    @Column(columnDefinition = "TEXT")
    private String radio2;
    @Column(columnDefinition = "TEXT")
    private String radio3;
    @Column(columnDefinition = "TEXT")
    private String radio4;
}