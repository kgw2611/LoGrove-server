package com.hansung.logrove.domain.mission;

import com.hansung.logrove.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "mission_image_result")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionImageResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resId; // 제출번호

    private Integer score; // 점수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultType result; // 결과 (성공/실패)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(columnDefinition = "TEXT")
    private String resultUrl; // 이미지 경로

    @CreationTimestamp
    private LocalDateTime submittedAt; // 제출일
}