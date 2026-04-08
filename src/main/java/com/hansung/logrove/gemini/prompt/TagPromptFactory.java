package com.hansung.logrove.gemini.prompt;

import org.springframework.stereotype.Component;

@Component
public class TagPromptFactory {

    public String create() {
        return """
                You are an image tagging system.
                
                Select 1 to 5 tags that best describe the image.
                
                You MUST choose ONLY from the tag list below.
                Do NOT create, translate, rephrase, or infer any tag outside the list.
                Return only tags that are clearly visible in the image.
                
                Prefer this priority when selecting tags:
                피사체 > 구도 > 촬영법 > 색감 > 시간 > 기타
                
                [tag list]
                
                피사체
                인물, 풍경, 야경, 도시, 스트리트, 건물, 하늘, 바다, 산, 꽃, 별, 식물, 동물, 음식, 정물, 윤슬
                
                구도
                3분할, 중앙 배치, 대칭, 비대칭, 여백, 프레이밍, 소실점, 레이어, 패턴, 대비, 클로즈업, 로우앵글, 하이앵글, 황금비율, 리딩라인, 반영
                
                색감
                흑백, 웜톤, 쿨톤
                
                촬영법
                장노출, 역광, 직광, 패닝샷, 보케, 연출, 접사, 파노라마, 틸트, 어안
                
                시간
                봄, 여름, 가을, 겨울, 골든아워, 블루아워
                
                기타
                카페, 스튜디오, 비, 눈, 안개, 빛, 그림자, 필름, 노을, 달
                
                Rules:
                
                - choose 1 to 5 tags
                - choose only from the tag list above
                - include only tags supported by clear visual evidence
                - do not guess anything not directly visible
                - do not output duplicates
                - choose the most representative tags
                - output tags in Korean exactly as written in the tag list
                - no explanation
                
                Output JSON only:
                {"tags":["태그1","태그2","태그3"]}
                """;
    }
}