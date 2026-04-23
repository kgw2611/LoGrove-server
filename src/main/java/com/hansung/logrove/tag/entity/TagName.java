package com.hansung.logrove.tag.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TagName {

    // 커뮤니티
    DAILY("일상"), TRADE("거래"), INFO("정보"), QUESTION("질문"),
    PHOTO("사진"), LOCATION("출사지"), EVENT("이벤트"), REVIEW("후기"),

    // 포럼
    CANON("캐논"), SONY("소니"), NIKON("니콘"), FUJIFILM("후지필름"),
    LEICA("라이카"), HASSELBLAD("핫셀블라드"), PANASONIC("파나소닉"),
    OLYMPUS("올림푸스"), OTHER("기타"),

    // 갤러리 - 피사체
    PERSON("인물"), LANDSCAPE("풍경"), NIGHT_VIEW("야경"), CITY("도시"),
    STREET("스트리트"), ARCHITECTURE("건축물"), SKY("하늘"), SEA("바다"),
    MOUNTAIN("산"), FLOWER("꽃"), STAR("별"), PLANT("식물"),
    ANIMAL("동물"), FOOD("음식"), STILL_LIFE("정물"), YUNSUL("윤슬"),

    // 갤러리 - 구도
    RULE_OF_THIRDS("3분할"), CENTER("중앙배치"), SYMMETRY("대칭"),
    ASYMMETRY("비대칭"), NEGATIVE_SPACE("여백"), FRAMING("프레이밍"),
    VANISHING_POINT("소실점"), LAYER("레이어"), PATTERN("패턴"),
    CONTRAST("대비"), CLOSE_UP("클로즈업"), LOW_ANGLE("로우앵글"),
    HIGH_ANGLE("하이앵글"), GOLDEN_RATIO("황금비율"), LEADING_LINE("리딩라인"),
    REFLECTION("반영"),

    // 갤러리 - 색감
    BLACK_AND_WHITE("흑백"), WARM_TONE("웜톤"), COOL_TONE("쿨톤"),

    // 갤러리 - 촬영법
    LONG_EXPOSURE("장노출"), BACKLIGHT("역광"), DIRECT_LIGHT("직광"),
    PANNING("패닝샷"), BOKEH("보케"), STAGED("연출"), MACRO("접사"),
    PANORAMA("파노라마"), TILT("틸트"), FISHEYE("어안"),

    // 갤러리 - 시간
    SPRING("봄"), SUMMER("여름"), AUTUMN("가을"), WINTER("겨울"),
    GOLDEN_HOUR("골든아워"), BLUE_HOUR("블루아워"),

    // 갤러리 - 기타
    CAFE("카페"), STUDIO("스튜디오"), RAIN("비"), SNOW("눈"),
    FOG("안개"), LIGHT("빛"), SHADOW("그림자"), FILM("필름"),
    SUNSET("노을"), MOON("달");

    private final String korean;

    TagName(String korean) {
        this.korean = korean;
    }

    @JsonValue
    public String getKorean() {
        return korean;
    }

    /** 한글 태그명 → enum 변환. 매칭 없으면 null 반환 */
    @JsonCreator
    public static TagName fromKorean(String value) {
        for (TagName tag : values()) {
            if (tag.korean.equals(value) || tag.name().equals(value)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("Unknown tag: " + value);
    }
}