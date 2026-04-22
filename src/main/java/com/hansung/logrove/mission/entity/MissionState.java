package com.hansung.logrove.mission.entity;

import com.hansung.logrove.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "mission_state")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@IdClass(MissionState.MissionStateId.class) // 1. 클래스 내부의 식별자 클래스를 ID로 지정
public class MissionState {

    @Id // 2. 복합키의 첫 번째 구성 요소
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Id // 3. 복합키의 두 번째 구성 요소
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private MissionStatus state;

    // 비즈니스 로직: 미션 상태 변경
    public void updateStatus(MissionStatus state) {
        this.state = state;
    }
    /**
     * [내부 식별자 클래스]
     * 새로운 파일을 만들지 않고, 엔티티 안에 static 클래스로 정의하여 관리합니다.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class MissionStateId implements Serializable {
        private Long mission; // 필드명을 위 엔티티의 필드명과 맞춰야 합니다.
        private Long user;
    }
}