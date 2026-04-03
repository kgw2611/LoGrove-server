package com.hansung.logrove.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MISSIONS") // 1. ERD에 명시된 테이블명 매핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 2. 무분별한 객체 생성 방지
@AllArgsConstructor
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id") // 3. PK 필드명 설정
    private Long id;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT") // 4. 긴 지문을 위한 TEXT 타입
    private String question;

    @Column(name = "point", nullable = false) // 5. 미션 성공 시 지급할 경험치
    @Builder.Default
    private Integer point = 0;

    @OneToOne(mappedBy = "mission", fetch = FetchType.LAZY)
    private MissionStair missionStair;

    @OneToOne(mappedBy = "mission", fetch = FetchType.LAZY)
    private MissionImage missionImage;

    // 비즈니스 로직: 미션 정보 수정이 필요할 때 사용
    public void updateMission(String question, Integer point) {
        this.question = question;
        this.point = point;
    }
}