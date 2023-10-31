package com.gwakkili.devbe.report.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member reporter;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String content;


    @Getter
    @RequiredArgsConstructor
    public enum Type {

        PROFANITY("욕설/비방 및 개인정보 노출"),
        PORNOGRAPHY("음란성 및 청소년에게 부적절한 내용"),
        DISCOMFORT("불쾌감 및 분란 유발"),
        WRONG_INFO("잘못된 정보");

        private final String description;

        @JsonCreator
        public static Type from(String val){
            return Arrays.stream(values())
                    .filter(boardType -> String.valueOf(boardType).equals(val))
                    .findAny()
                    .orElse(null);
        }
    }
}
