package com.hansung.logrove.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    private String profileImageUrl;

    @Builder.Default
    private Integer exp = 0;

    @Builder.Default
    private Integer level = 1;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserRole> roles = new ArrayList<>();

    // 권한 추가 편의 메서드
    public void addRole(RoleType roleType) {
        UserRole userRole = UserRole.builder()
                .roleName(roleType)
                .user(this)
                .build();
        this.roles.add(userRole);
    }
}