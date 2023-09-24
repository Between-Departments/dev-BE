package com.gwakkili.devbe.entity.report;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.entity.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@MappedSuperclass
@Getter
public class Report extends BaseEntity {
    @RequiredArgsConstructor
    @Getter
    public enum Type {

        PROFANITY("욕설/비방 및 개인정보 노출"),
        PORNOGRAPHY("음란성 및 청소년에게 부적절한 내용"),
        DISCOMFORT("불쾌감 및 분란 유발"),
        WRONG_INFO("잘못된 정보");

        private final String description;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member reporter;

    @Enumerated(value = EnumType.STRING)
    private Type Type;

    private String content;
}
