package com.hansung.logrove.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [시나리오 4: 정답 제출용 DTO]
 * 유저가 화면에서 입력한 답을 서버로 보낼 때 사용하는 바구니입니다.
 */
@Getter
@NoArgsConstructor // JSON 데이터를 객체로 변환할 때 필수!
@AllArgsConstructor // 테스트나 빌더를 위해 모든 필드 생성자 추가
public class MissionSubmitRequest {

    // 어떤 미션에 대한 정답인지 구분하기 위한 ID
    private Long missionId;

    // 유저가 실제로 입력하거나 선택한 정답 텍스트
    // 객관식이면 "1", "2" 등이 들어오고, 주관식이면 유저가 타이핑한 글자가 들어옵니다.
    private String submittedAnswer;
}