package com.hansung.logrove.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_ROLE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role", nullable = false, length = 40)
    private String role;
}