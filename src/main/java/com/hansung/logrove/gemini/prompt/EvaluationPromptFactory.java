package com.hansung.logrove.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class EvaluationPromptFactory {

    public String create(String theme, String content, String guide) {
        return """
                너는 사진 미션 채점관이야. 아래 미션 조건에 따라 제출된 사진을 평가해줘.
                
                [미션 정보]
                주제: %s
                설명: %s
                촬영 가이드: %s
                
                [채점 기준]
                - 미션 주제와 사진의 일치도
                - 촬영 가이드 준수 여부
                - 사진의 전반적인 완성도
                
                [출력 규칙]
                반드시 아래 JSON 형식으로만 출력하고 다른 텍스트는 절대 포함하지 말 것.
                {
                  "score": 0~100 사이의 정수,
                  "feedback": "평가 코멘트 (한국어, 2~3문장)"
                }
                """.formatted(theme, content, guide);
    }
}