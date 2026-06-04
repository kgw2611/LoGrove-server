package com.hansung.logrove.mission.service;

import com.hansung.logrove.gemini.client.GeminiClient;
import com.hansung.logrove.gemini.dto.CasualEvaluationResponse;
import com.hansung.logrove.gemini.parser.GeminiResponseParser;
import com.hansung.logrove.gemini.prompt.CasualEvaluationPromptFactory;
import com.hansung.logrove.global.exception.ErrorCode;
import com.hansung.logrove.global.exception.LoGroveException;
import com.hansung.logrove.mission.dto.CasualResultResponse;
import com.hansung.logrove.mission.entity.Mission;
import com.hansung.logrove.mission.entity.MissionImageResult;
import com.hansung.logrove.mission.entity.MissionResultStatus;
import com.hansung.logrove.mission.repository.MissionImageResultRepository;
import com.hansung.logrove.mission.repository.MissionRepository;
import com.hansung.logrove.storage.dto.ImageUploadResult;
import com.hansung.logrove.storage.service.ImageStorageService;
import com.hansung.logrove.user.entity.User;
import com.hansung.logrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CasualPhotoService {

    private static final Long CASUAL_MISSION_ID = 57L;

    private final GeminiClient geminiClient;
    private final CasualEvaluationPromptFactory promptFactory;
    private final GeminiResponseParser parser;
    private final ImageStorageService imageStorageService;
    private final MissionImageResultRepository resultRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    @Transactional
    public CasualResultResponse evaluate(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new LoGroveException(ErrorCode.INVALID_INPUT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LoGroveException(ErrorCode.USER_NOT_FOUND));
        Mission casualMission = missionRepository.findById(CASUAL_MISSION_ID)
                .orElseThrow(() -> new LoGroveException(ErrorCode.MISSION_NOT_FOUND));

        String rawResponse = geminiClient.requestEvaluation(file, promptFactory.create());
        CasualEvaluationResponse evaluation = parser.parseCasualResponse(rawResponse);

        ImageUploadResult uploaded = imageStorageService.storeSubmissionImage(userId, file);

        MissionImageResult record = MissionImageResult.builder()
                .score(evaluation.getScore())
                .result(MissionResultStatus.PASS)
                .user(user)
                .mission(casualMission)
                .resultUrl(uploaded.getUrl())
                .build();
        MissionImageResult saved = resultRepository.save(record);

        return new CasualResultResponse(
                saved.getId(),
                saved.getScore(),
                evaluation.getReason(),
                evaluation.getScoreReason(),
                saved.getResultUrl(),
                saved.getSubmittedAt()
        );
    }
}
