package com.gwakkili.devbe.major.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long majorId;

    @Enumerated(EnumType.STRING)
    Category category;

    @Column(unique = true)
    String name;

    @RequiredArgsConstructor
    public enum Category{
        SOCIAL("인문/사회 계열"),
        ENGINEERING("공학 계열"),
        EDUCATION("교육 계열"),
        PHYSICAL("예체능 계열"),
        MEDICAL("의학 계열"),
        ETC("기타");

        private final String description;

        @JsonCreator
        public static Category from(String val){
            return Arrays.stream(values())
                    .filter(category -> String.valueOf(category).equals(val))
                    .findAny()
                    .orElse(null);
        }
    }
}
