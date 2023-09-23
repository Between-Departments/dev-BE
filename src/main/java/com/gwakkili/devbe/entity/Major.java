package com.gwakkili.devbe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Major {


    @Id
    @Column(name = "major_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    Category category;

    String name;

    @RequiredArgsConstructor
    enum Category{
        SOCIAL("사회 계열"),
        ENGINEERING("공학 계열"),
        EDUCATION("교육 계열"),
        PHYSICAL("예체능 계열"),
        MEDICAL("의학 계열");

        private final String description;
    }
}
