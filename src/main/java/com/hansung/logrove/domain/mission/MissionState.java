package com.hansung.logrove.domain.mission;

import com.hansung.logrove.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mission_state")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionState {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StateType state; // ENUM: 진행중, 완료 등
}