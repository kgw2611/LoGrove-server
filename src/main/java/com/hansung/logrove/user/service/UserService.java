package com.hansung.logrove.user.service;

import com.hansung.logrove.global.exception.ErrorCode;
import com.hansung.logrove.global.exception.LoGroveException;
import com.hansung.logrove.post.dto.PostListResponse;
import com.hansung.logrove.post.repository.PostRepository;
import com.hansung.logrove.storage.dto.ImageUploadResult;
import com.hansung.logrove.storage.service.ImageStorageService;
import com.hansung.logrove.user.dto.SignUpRequest;
import com.hansung.logrove.user.dto.UserResponse;
import com.hansung.logrove.user.dto.UserUpdateRequest;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.entity.UserRole;
import com.hansung.logrove.user.repository.UserRepository;
import com.hansung.logrove.user.repository.UserRoleRepository;
import com.hansung.logrove.user.dto.GameProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final ImageStorageService imageStorageService;

    // 회원가입 - loginId/닉네임 중복 체크 후 BCrypt 인코딩하여 저장
    @Transactional
    public UserResponse register(SignUpRequest request) {

        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new LoGroveException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new LoGroveException(ErrorCode.DUPLICATE_NICKNAME);
        }

        UserRole role = userRoleRepository.findByRole("USER")
                .orElseThrow(() -> new LoGroveException(ErrorCode.INTERNAL_SERVER_ERROR));

        User user = User.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .loginId(request.getLoginId())
                .loginPw(passwordEncoder.encode(request.getLoginPw()))
                .email(request.getEmail())
                .role(role)
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    // 마이페이지 조회
    @Transactional(readOnly = true)
    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    // 회원 정보 수정
    @Transactional
    public UserResponse updateMyInfo(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));

        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new LoGroveException(ErrorCode.DUPLICATE_NICKNAME);
        }

        user.updateProfile(request.getNickname(), request.getEmail());
        return UserResponse.from(user);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    // 게임 이력 프로필 조회
    @Transactional(readOnly = true)
    public GameProfileResponse getGameProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        return GameProfileResponse.from(user);
    }

    // 프로필 이미지 변경
    @Transactional
    public UserResponse updateProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        ImageUploadResult result = imageStorageService.storeProfileImage(userId, file);
        user.updateProfileImage(result.getUrl());
        return UserResponse.from(user);
    }

    // 내가 작성한 게시글 목록 - /api/users/me/myposts
    @Transactional(readOnly = true)
    public List<PostListResponse> getMyPosts(Long userId) {
        return postRepository.findByUser_Id(userId).stream()
                .map(PostListResponse::from)
                .toList();
    }
}