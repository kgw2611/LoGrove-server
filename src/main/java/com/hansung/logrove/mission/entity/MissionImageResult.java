package com.hansung.logrove.mission.entity;

import com.hansung.logrove.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "missions_image_result")
@EntityListeners(AuditingEntityListener.class) // 1. 생성 시간 자동 기록 설정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MissionImageResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    private Long id;

    @Column(name = "score", nullable = false) // 2. AI가 판정한 점수
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false) // 3. SUCCESS 또는 FAIL
    private MissionResultStatus result;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "result_url", columnDefinition = "TEXT") // 4. S3에 저장된 유저 업로드 사진 경로
    private String resultUrl;

    @CreatedDate
    @Column(name = "submitted_at", nullable = false, updatable = false) // 5. 제출 일시
    private LocalDateTime submittedAt;
}