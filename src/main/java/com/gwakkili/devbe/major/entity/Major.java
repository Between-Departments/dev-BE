package com.gwakkili.devbe.major.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long majorId;

    @Enumerated(EnumType.STRING)
    Category category;

    @Column(unique = true)
    String name;

    @Builder
    public Major(Category category, String name) {
        this.category = category;
        this.name = name;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Category {
        SOCIAL("인문/사회 계열"),
        ENGINEERING("공학 계열"),
        EDUCATION("교육 계열"),
        PHYSICAL("예체능 계열"),
        MEDICAL("의학 계열"),
        ETC("기타");

        private final String description;

    }
}
