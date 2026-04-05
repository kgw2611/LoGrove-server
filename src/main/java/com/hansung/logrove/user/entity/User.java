package com.hansung.logrove.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "login_pw", nullable = false, length = 100)
    private String loginPw;

    @Column(name = "login_id", nullable = false, length = 100)
    private String loginId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private UserRole role;

    @Column(name = "exp", nullable = false)
    @Builder.Default
    private Integer exp = 0;

    @Column(name = "level", nullable = false)
    @Builder.Default
    private Integer level = 1;

    @Column(name = "progress", nullable = false)
    @Builder.Default
    private Integer progress = 0;

    // 비즈니스 메서드
    public void updateProfile(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public void addExp(int amount) {
        this.exp += amount;
    }
}