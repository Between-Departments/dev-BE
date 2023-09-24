package com.gwakkili.devbe.entity.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReportType {

    PROFANITY("욕설/비방 및 개인정보 노출"),
    PORNOGRAPHY("음란성 및 청소년에게 부적절한 내용"),
    DISCOMFORT("불쾌감 및 분란 유발"),
    WRONG_INFO("잘못된 정보");

    private final String description;
}
