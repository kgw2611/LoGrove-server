package com.hansung.logrove.user.service;

import com.hansung.logrove.global.exception.ErrorCode;
import com.hansung.logrove.global.exception.LoGroveException;
import com.hansung.logrove.mission.entity.MissionImageResult;
import com.hansung.logrove.mission.repository.MissionImageResultRepository;
import com.hansung.logrove.post.dto.PostListResponse;
import com.hansung.logrove.post.entity.PostImage;
import com.hansung.logrove.post.repository.PostImageRepository;
import com.hansung.logrove.post.repository.PostRepository;
import com.hansung.logrove.user.dto.MyGalleryImageResponse;
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

    private static final int[] LEVEL_THRESHOLDS = {0, 500, 1500, 3000, 5500, 9000, 13300};

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final MissionImageResultRepository missionImageResultRepository;
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

    @Transactional
    public void addExp(Long userId, int gained) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        user.addExp(gained);
        user.updateLevel(calcLevel(user.getExp()));
    }

    private int calcLevel(int totalExp) {
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (totalExp >= LEVEL_THRESHOLDS[i]) return i + 1;
        }
        return 1;
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

        String newNickname = request.getNickname();
        if (newNickname != null && !newNickname.isBlank() &&
                !user.getNickname().equals(newNickname) &&
                userRepository.existsByNickname(newNickname)) {
            throw new LoGroveException(ErrorCode.DUPLICATE_NICKNAME);
        }

        user.updateProfile(request.getNickname(), request.getEmail(), request.getBio());
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

    // 나의 갤러리 - 갤러리 게시글 이미지 + 미션 제출 이미지
    @Transactional(readOnly = true)
    public List<MyGalleryImageResponse> getMyGallery(Long userId) {
        List<MyGalleryImageResponse> result = new java.util.ArrayList<>();

        postImageRepository.findByPostUserIdAndPostBoardTypeBoard(userId, "GALLERY").stream()
                .map(img -> new MyGalleryImageResponse(
                        img.getId(), img.getUrl(), "gallery",
                        img.getPost().getTitle(), img.getPost().getCreatedAt(), img.getPost().getId()))
                .forEach(result::add);

        missionImageResultRepository.findByUserId(userId).stream()
                .filter(r -> r.getResultUrl() != null)
                .map(r -> new MyGalleryImageResponse(
                        r.getId(), r.getResultUrl(), "mission",
                        r.getMission().getMissionImage().getTheme(), r.getSubmittedAt(), r.getId()))
                .forEach(result::add);

        result.sort(java.util.Comparator.comparing(
                MyGalleryImageResponse::getCreatedAt,
                java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())));

        return result;
    }
}