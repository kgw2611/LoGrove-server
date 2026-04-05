package com.hansung.logrove.auth.service;

import com.hansung.logrove.auth.dto.LoginRequest;
import com.hansung.logrove.auth.dto.LoginResponse;
import com.hansung.logrove.global.exception.ErrorCode;
import com.hansung.logrove.global.exception.LoGroveException;
import com.hansung.logrove.global.jwt.JwtUtil;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 로그인 처리
     * 1. loginId로 사용자 조회
     * 2. 비밀번호 BCrypt 검증
     * 3. 검증 성공 시 JWT 발급
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        // 아이디로 사용자 조회 (없으면 인증 실패)
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new LoGroveException(ErrorCode.INVALID_CREDENTIALS));

        // BCrypt로 비밀번호 검증
        if (!passwordEncoder.matches(request.getLoginPw(), user.getLoginPw())) {
            throw new LoGroveException(ErrorCode.INVALID_CREDENTIALS);
        }

        // JWT 발급 (subject = userId)
        String token = jwtUtil.generateToken(user.getId());

        return new LoginResponse(token, user.getNickname(), user.getId());
    }
}