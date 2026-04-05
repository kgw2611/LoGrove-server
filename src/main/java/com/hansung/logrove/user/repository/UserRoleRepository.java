package com.hansung.logrove.user.repository;

import com.hansung.logrove.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByRole(String role);
}