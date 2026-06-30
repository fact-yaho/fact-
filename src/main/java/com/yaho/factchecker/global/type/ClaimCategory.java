package com.yaho.factchecker.global.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClaimCategory {
    BILATERAL_RELATIONS("양자관계"),
    NORTH_KOREA_RELATIONS("대북관계"),
    HIGH_LEVEL_EXCHANGE("고위급 교류·정상외교"),
    TREATY_AGREEMENT("조약·협정"),
    OFFICIAL_POSITION("공식 입장·브리핑·논평"),
    DIPLOMATIC_POLICY("외교 정책·기조"),
    INTERNATIONAL_AFFAIRS("국제정세·다자외교"),
    ECONOMIC_DIPLOMACY("통상·경제외교"),
    DEVELOPMENT_COOPERATION("개발협력·ODA");

    private final String label;

}
