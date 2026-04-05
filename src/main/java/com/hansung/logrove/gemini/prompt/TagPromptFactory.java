package com.hansung.logrove.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class TagPromptFactory {

    public String create() {
        return """
                아래 태그 목록 중에서 이 사진에 해당하는 태그를 선택해줘.
                
                [태그 목록]
                PORTRAIT, LANDSCAPE, STREET, ANIMAL, FOOD, ARCHITECTURE, NATURE,
                WARM_TONE, COOL_TONE, BLACK_AND_WHITE, VIVID, GOLDEN_HOUR, NIGHT,
                SUNRISE, SUNSET, RAINY, BOKEH, LONG_EXPOSURE, MACRO, SILHOUETTE
                
                [규칙]
                - 반드시 위 목록에 있는 태그만 사용할 것
                - 쉼표로 구분하여 1~5개만 선택할 것
                - 태그 이름만 출력하고 다른 설명은 절대 포함하지 말 것
                
                출력 예시: PORTRAIT, WARM_TONE, BOKEH
                """;
    }
}